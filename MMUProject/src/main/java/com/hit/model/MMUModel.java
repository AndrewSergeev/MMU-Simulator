package com.hit.model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.hit.algorithm.IAlgoCache;
import com.hit.algorithm.LRUAlgoCacheImpl;
import com.hit.algorithm.MRUAlgoCacheImpl;
import com.hit.algorithm.RandomCacheImpl;
import com.hit.memoryunits.MemoryManagementUnit;
import com.hit.processes.Process;
import com.hit.processes.ProcessCycles;
import com.hit.processes.RunConfiguration;
import com.hit.util.MMULogger;

public class MMUModel extends Observable implements Model{
	public static MemoryManagementUnit mmu;
	private int	numProcesses; 
	private int	ramCapacity; 
	
	public MMUModel(){	}	
	
	public void start(String[] command) throws InterruptedException {
		
		IAlgoCache<Long, Long> algo = null;

		ramCapacity = Integer.parseInt(command[1]);
		MMULogger.getInstance().write(MessageFormat.format("RC:{0}\n", ramCapacity), Level.INFO);
		if(command[0].equalsIgnoreCase("LRU"))
		{
			algo = new LRUAlgoCacheImpl<Long,Long>(ramCapacity);
		}
		else if(command[0].equalsIgnoreCase("MRU"))
		{
			algo = new MRUAlgoCacheImpl<Long,Long>(ramCapacity);
		}
		else if(command[0].equalsIgnoreCase("Random"))
		{
			algo = new RandomCacheImpl<Long,Long>(ramCapacity);
		}		

		mmu = new MemoryManagementUnit(ramCapacity, algo);
		RunConfiguration runConfig = readConfigurationFile();
		List<ProcessCycles> processCycles = runConfig.getProcessesCycles();
		List<Process> processes = createProcesses(processCycles, mmu);
		numProcesses = processes.size();
		MMULogger.getInstance().write(MessageFormat.format("PN:{0}\n", numProcesses), Level.INFO);
		runProcesses(processes);
		setChanged();
		notifyObservers(this);
	}	
	
	public static RunConfiguration readConfigurationFile(){
		RunConfiguration runConfig = null;
		Gson gson = new Gson();
		JsonReader reader;

		try {
			reader = new JsonReader(new FileReader("src/main/resources/com/hit/config/Configuration.json"));
			runConfig = gson.fromJson(reader, RunConfiguration.class);
		} catch (FileNotFoundException e) {	
			MMULogger.getInstance().write(e.getMessage(), Level.SEVERE);
			e.printStackTrace();
		}

		return runConfig;		
	}	

	public static void runProcesses(List<Process> applications) {
		ExecutorService executor = Executors.newCachedThreadPool();
		List<Future<Boolean>> futureList = new ArrayList<Future<Boolean>>();
		for(Process process : applications)
		{			
			Future<Boolean> fut = executor.submit((Callable<Boolean>) process);
			futureList.add(fut);
		}		

		try {
			executor.awaitTermination(4, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			MMULogger.getInstance().write(e.getMessage(), Level.SEVERE);
			e.printStackTrace();
		}
		
		mmu.shutDown();		
		executor.shutdown();		
	}

	public static List<Process> createProcesses(List<ProcessCycles> appliocationsScenarios, MemoryManagementUnit mmu){
		List<Process> processes = new ArrayList<Process>();
		int index = 1;

		for(ProcessCycles procCycles : appliocationsScenarios)
		{
			Process process = new Process(index, mmu, procCycles);
			processes.add(process);
			index++;
		}

		return processes;
	}
	
	public List<String> getCommandsFromLogFile() {
		List<String> commands = new ArrayList<String>();
		
		try {
			commands = Files.readAllLines(Paths.get(MMULogger.DEFAULT_FILE_NAME));
		} catch (IOException e) {
			MMULogger.getInstance().write(e.getMessage(), Level.SEVERE);
			e.printStackTrace();
		}
		
		return commands;
	}

	public int getNumProcesses() {
		return numProcesses;
	}

	public void setNumProcesses(int numProcesses) {
		this.numProcesses = numProcesses;
	}

	public int getRamCapacity() {
		return ramCapacity;
	}

	public void setRamCapacity(int ramCapacity) {
		this.ramCapacity = ramCapacity;
	}	
}
