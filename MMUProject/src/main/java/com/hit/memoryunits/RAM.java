package com.hit.memoryunits;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class RAM {
	private int initialCapacity;
	private Map<Long,Page<byte[]>> pagesInRAM;
	
	RAM(int initialCapacity) {
		this.initialCapacity = initialCapacity;
		pagesInRAM = new LinkedHashMap<Long,Page<byte[]>>(initialCapacity, 0.75f, true);
	}
	
	public void addPage(Page<byte[]> addPage) {
		if(addPage != null)
		{
			pagesInRAM.put(addPage.getPageId(), addPage);
		}
	}
	
	public void	addPages(Page<byte[]>[] addPages) {
		for(Page<byte[]> page : addPages)
		{
			addPage(page);
		}
	}
		
	public Page<byte[]>	getPage(Long pageId) {
		return pagesInRAM.getOrDefault(pageId, null); 
	}	
	
	@SuppressWarnings("unchecked")
	public Page<byte[]>[] getPages(Long[] pageIds) { 
		ArrayList<Page<byte[]>> requestedPages = new ArrayList<Page<byte[]>>();
		
		for(Long pageId : pageIds)
		{			
			Page<byte[]> page = getPage(pageId);
			if(page != null)
			{
				requestedPages.add(page);
			}			
		}		
		
		return requestedPages.toArray(new Page[requestedPages.size()]);
	}
	
	public boolean isFull()	{
		boolean ifFull = true;
		
		if(pagesInRAM.size() < this.initialCapacity)
		{
			ifFull = false;
		}
		
		return ifFull;
	}
	
	public void removePage(Page<byte[]> removePage) {		
			pagesInRAM.remove(removePage.getPageId());			
	}
	
	public void removePages(Page<byte[]>[] removePages) {
		for(Page<byte[]> page : removePages)
		{
			removePage(page);
		}
	}	
	
	public void setPages(Map<Long,Page<byte[]>> pages) {
		this.pagesInRAM.putAll(pages);		
	}	

	public Map<Long,Page<byte[]>> getPages() {		
		return this.pagesInRAM;
	}
	

	public void setInitialCapacity(int initialCapacity) {
		this.initialCapacity = initialCapacity;
	}

	public int getInitialCapacity() {		
		return this.initialCapacity;
	}
}
