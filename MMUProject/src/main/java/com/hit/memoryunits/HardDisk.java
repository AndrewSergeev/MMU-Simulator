package com.hit.memoryunits;

import com.hit.exception.HardDiskException;
//import com.hit.exception.HardDiskException;
import com.hit.util.MMULogger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class HardDisk {
	private final static int _SIZE = 1000;
	private final static String DEFAULT_FILE_NAME = "HardDiskFile";
	private static HardDisk hardDisk;	
	private Map<Long,Page<byte[]>> pagesInHD;	

	public static HardDisk getInstance() {
		if(hardDisk == null)
		{
			hardDisk = new HardDisk();
		}
		
		return hardDisk;
	}
	
	private HardDisk()
	{			
		pagesInHD = new HashMap<Long,Page<byte[]>>();		
		for(Long i = 0L; i < _SIZE; i++)
		{
			pagesInHD.put(i, new Page<byte[]>(i, null));
		}
		
		try {
			loadDataToFile();			
		} catch (IOException e) {
			MMULogger.getInstance().write(e.getMessage(), Level.SEVERE);
			e.printStackTrace();
		}
	}
	
	public Page<byte[]> pageFault(Long pageId)throws HardDiskException 
	{	
		Page<byte[]> pageFromHdToRam = null;	
		
		try {
			loadDataToMap();
			pageFromHdToRam = pagesInHD.get(pageId);
			pagesInHD.remove(pageId);
			loadDataToFile();			
		} catch (IOException e) {
			MMULogger.getInstance().write(e.getMessage(), Level.SEVERE);
			//System.out.println(e);
			throw new HardDiskException("Error! Can't load data to map.", HardDiskException.ActionError.PAGE_FAULT);
		}			
		
		return pageFromHdToRam;
	}
	
	public Page<byte[]> pageReplacement(Page<byte[]> moveToHdPage, Long moveToRamId) throws HardDiskException 
	{
		Page<byte[]> pageFromHdToRam = null;		
		
		try {
			loadDataToMap();
		} catch (IOException e) {
			MMULogger.getInstance().write(e.getMessage(), Level.SEVERE);
			throw new HardDiskException("Error! Can't load data to map.", HardDiskException.ActionError.PAGE_REPLACEMENT);			
		}
		
		pageFromHdToRam = pagesInHD.get(moveToRamId);
		pagesInHD.remove(moveToRamId);
		if(_SIZE <= pagesInHD.size())
		{
			MMULogger.getInstance().write("Error! There is no free space in Hard Disk.", Level.SEVERE);
		}
		else if(!pagesInHD.containsKey(moveToHdPage.getPageId()))
		{
			pagesInHD.put(moveToHdPage.getPageId(), moveToHdPage);
		}
		
		try {
			loadDataToFile();
		} catch (IOException e) {
			MMULogger.getInstance().write(e.getMessage(), Level.SEVERE);
			e.printStackTrace();
		}
		
		return pageFromHdToRam;		
	}
	
	@SuppressWarnings("unchecked")
	public void loadDataToMap() throws FileNotFoundException, IOException
	{
		FileInputStream fInput = new FileInputStream(DEFAULT_FILE_NAME);
		ObjectInputStream input = new ObjectInputStream(fInput);
		pagesInHD.clear();
		try {
			pagesInHD = (HashMap<Long,Page<byte[]>>) input.readObject();
		} catch (ClassNotFoundException e) {	
			MMULogger.getInstance().write(e.getMessage(), Level.SEVERE);
			e.printStackTrace();
		} finally{
			fInput.close();
			input.close();
		}		
	}
	
	public void loadDataToFile() throws FileNotFoundException, IOException 
	{
		FileOutputStream fOutput = new FileOutputStream(DEFAULT_FILE_NAME);
		ObjectOutputStream output = new ObjectOutputStream(fOutput);
		output.writeObject(pagesInHD);	
		fOutput.close();
		output.close();
	}	

	public Map<Long, Page<byte[]>> getPagesInHD() {
		return pagesInHD;
	}	
}
