package com.hit.processes;

import java.util.List;

public class RunConfiguration {
	private List<ProcessCycles> processesCycles;
	
	public RunConfiguration(List<ProcessCycles> processesCycles){
		setProcessesCycles(processesCycles);
	}
	
	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder();
		
		for(ProcessCycles cycles : processesCycles)
		{
			sb.append(cycles);
			sb.append("\n");
		}
		
		return sb.toString();
	}

	public List<ProcessCycles> getProcessesCycles() {
		return processesCycles;
	}

	public void setProcessesCycles(List<ProcessCycles> processesCycles) {
		this.processesCycles = processesCycles;
	}

}
