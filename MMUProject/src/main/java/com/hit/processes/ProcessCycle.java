package com.hit.processes;

import java.util.Arrays;
import java.util.List;

public class ProcessCycle {
	private List<Long> pages;
	private int sleepMs;
	private List<byte[]> data;

	public ProcessCycle(List<Long> pages, int sleepMs, List<byte[]> data){
		setPages(pages);
		setSleepMs(sleepMs);
		setData(data);
	}
	
	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder();
		
		for(int i = 0; i < pages.size(); i++)
		{
			sb.append("Page Id: ");
			sb.append(pages.toArray()[i]);
			sb.append("Ms to sleep: ");
			sb.append(String.valueOf(sleepMs));
			sb.append("Data: ");
			sb.append(Arrays.toString((byte[]) this.data.toArray()[i]));
			sb.append("\n");
		}
		
		return sb.toString();
	}
	
	public List<Long> getPages() {
		return pages;
	}

	public void setPages(List<Long> pages) {
		this.pages = pages;
	}

	public List<byte[]> getData() {
		return data;
	}

	public void setData(List<byte[]> data) {
		this.data = data;
	}

	public int getSleepMs() {
		return sleepMs;
	}

	public void setSleepMs(int sleepMs) {
		this.sleepMs = sleepMs;
	}
}
