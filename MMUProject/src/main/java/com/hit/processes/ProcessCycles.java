package com.hit.processes;

import java.util.List;

public class ProcessCycles {
	private List<ProcessCycle> processCycles;

	public ProcessCycles(List<ProcessCycle> processCycles){
		setProcessCycles(processCycles);
	}

	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder();
		
		for(ProcessCycle cycle : processCycles)
		{			
			sb.append(cycle.toString());
			sb.append("\n");
		}
		
		return sb.toString();
	}

	public List<ProcessCycle> getProcessCycles() {
		return processCycles;
	}

	public void setProcessCycles(List<ProcessCycle> processCycles) {
		this.processCycles = processCycles;
	}

}
