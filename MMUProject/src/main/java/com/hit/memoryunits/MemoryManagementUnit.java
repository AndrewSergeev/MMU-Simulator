package com.hit.memoryunits;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.logging.Level;

import com.hit.algorithm.IAlgoCache;
import com.hit.exception.HardDiskException;
import com.hit.util.MMULogger;

public class MemoryManagementUnit {
	private IAlgoCache<Long,Long> algo;
	private RAM ram;
	
	public MemoryManagementUnit(int ramCapacity, IAlgoCache<Long,Long> algo) {
		setAlgo(algo);
		this.ram = new RAM(ramCapacity);
	}

	@SuppressWarnings("unchecked")
	public Page<byte[]>[] getPages(Long[] pageIds) { 
		ArrayList<Page<byte[]>> requestedPages = new ArrayList<Page<byte[]>>();		
		ArrayList<Long> validPageIds = new ArrayList<Long>();
		ArrayList<Long> invalidPageIds = new ArrayList<Long>();
		Page<byte[]>[] pgsFromRam = null;		
		ArrayList<Page<byte[]>> pgsFromHD = null;
		
		for(Long pageId : pageIds)
		{
			Long id = algo.getElement(pageId);
			if(id != null)
			{
				validPageIds.add(pageId);
			}
			else
			{
				invalidPageIds.add(pageId);				
			}
		}		
		
		if(!validPageIds.isEmpty())
		{
			Long[] valids = makeLongArray(validPageIds);			
			pgsFromRam = ram.getPages(valids);
			for(Page<byte[]> page : pgsFromRam)
			{
				requestedPages.add(page);
			}
		}
		
		if(!invalidPageIds.isEmpty())
		{
			Long[] invalids = makeLongArray(invalidPageIds);			
			pgsFromHD = moveFromHdToRam(invalids);				
			requestedPages.addAll(pgsFromHD);					
		}
		
		return requestedPages.toArray(new Page[requestedPages.size()]);
	}	

	private ArrayList<Page<byte[]>> moveFromHdToRam(Long[] pageIds) {
		ArrayList<Page<byte[]>> requestedPagesFromHD = new ArrayList<Page<byte[]>>();
		Long valueIdFromPageTable;
		
		for(Long id : pageIds)
		{
			Page<byte[]> page = null;
			try {
				page = HardDisk.getInstance().pageFault(id);
				MMULogger.getInstance().write(MessageFormat.format("PF {0}\n", id), Level.INFO);
				valueIdFromPageTable = algo.putElement(page.getPageId(), page.getPageId());
				if(valueIdFromPageTable != null){
					Page<byte[]> replacedPageFromRAM = ram.getPage(valueIdFromPageTable);
					ram.removePage(replacedPageFromRAM);
					HardDisk.getInstance().pageReplacement(replacedPageFromRAM, page.getPageId());
					MMULogger.getInstance().write(MessageFormat.format("PR MTH {0} MTR {1}\n", replacedPageFromRAM.getPageId(), page.getPageId()), Level.INFO);
				}
				
				ram.addPage(page);				
			} catch (HardDiskException e) {
				MMULogger.getInstance().write(e.getMessage(), Level.SEVERE);
				e.printStackTrace();
			} 		
			
			requestedPagesFromHD.add(page);
		}
		
		return requestedPagesFromHD;		
	}	
	
	private Long[] makeLongArray(ArrayList<Long> arrayList) {
		Long[] array = new Long[arrayList.size()];
		
		for(int i = 0; i < arrayList.size(); i++)
		{
			array[i] = (Long) arrayList.toArray()[i];
		}
		
		return array;
	}

	public IAlgoCache<Long,Long> getAlgo() {
		return this.algo;
	}
	
	public RAM getRam() {
		return this.ram;
	}
	
	public void	setAlgo(IAlgoCache<Long,Long> algo) {
		this.algo = algo;
	}
	
	public void	setRam(RAM ram) {
		this.ram = ram;
	}
	
	public void	shutDown() {
		HardDisk.getInstance().getPagesInHD().putAll(ram.getPages());		
		try {
			HardDisk.getInstance().loadDataToFile();
		} catch (IOException e) {
			MMULogger.getInstance().write(e.getMessage(), Level.SEVERE);
			e.printStackTrace();
		}
		
		HardDisk.getInstance().getPagesInHD().clear();
		ram.getPages().clear();		
	}
	
	public void	update() {	}
}
