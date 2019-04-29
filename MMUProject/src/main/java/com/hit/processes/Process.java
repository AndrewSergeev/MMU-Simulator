package com.hit.processes;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.Level;

import com.hit.memoryunits.MemoryManagementUnit;
import com.hit.memoryunits.Page;
import com.hit.util.MMULogger;

public class Process implements Callable<Boolean>{	
	private MemoryManagementUnit mmu;
	private int ProcessId;
	private ProcessCycles processCycles;

	public Process(int id, MemoryManagementUnit mmu, ProcessCycles processCycles){
		setId(id);
		this.mmu = mmu;
		this.processCycles = processCycles;
	}

	@Override
	public Boolean call() throws Exception {
		Boolean bool = false;
		
		for(ProcessCycle cycle : processCycles.getProcessCycles())
		{
			synchronized(mmu){				
				List<Long> pages = cycle.getPages();
				List<byte[]> data = cycle.getData();
				Page<byte[]>[] pgsFromMMU = mmu.getPages(pages.toArray(new Long[pages.size()]));

				for(int i = 0; i < pages.size(); i++)
				{
					if(pgsFromMMU[i] != null)
					{
						pgsFromMMU[i].setContent((byte[]) data.toArray()[i]);
						MMULogger.getInstance().write(MessageFormat.format("GP:P{0} {1} {2}\n",
								this.ProcessId, pgsFromMMU[i].getPageId(), Arrays.toString((byte[]) data.toArray()[i])), Level.INFO);
					}
				}
			}
			
			Thread.sleep(cycle.getSleepMs());
		}

		bool = true;

		return bool;
	}

	public int getId()	{
		return this.ProcessId;
	}

	public void setId(int id)
	{
		this.ProcessId = id;
	}

}
