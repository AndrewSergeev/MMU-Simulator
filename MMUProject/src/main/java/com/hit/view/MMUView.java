package com.hit.view;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import javax.swing.JFrame;


public class MMUView extends Observable implements View {
	private List<String> commands;
	private List<String> pageReplacementCmds;
	private int	numProcesses; 
	private int	ramCapacity;
	private JFrame frame;
	private RAMTablePanel ramPanel;
	private CountersAndCommandsPanel countersAndCmdPanel;
	private ProcessesPanel procPanel;
	private ButtonsPanel buttonsPanel;
	private Integer lineIndex = 0;
	private String singleCmd = null;
	private boolean ifFiltered;
	
	public MMUView() {
		setCommands(new ArrayList<>());
		ifFiltered = false;
		pageReplacementCmds = new ArrayList<String>();
	}

	@Override
	public void start() {
		setChanged();
		notifyObservers();
		createAndShowGUI();		
	}

	protected void createAndShowGUI() {
		frame = new JFrame("MMU Analyser");		
		ramPanel = new RAMTablePanel(this);
		countersAndCmdPanel = new CountersAndCommandsPanel(this);
		buttonsPanel = new ButtonsPanel(this);
		procPanel = new ProcessesPanel(this);			
		
		frame.setLayout(new GridLayout(2,2));
		frame.setLocation(200, 250);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(ramPanel);
		frame.getContentPane().add(countersAndCmdPanel);	
		frame.getContentPane().add(procPanel);
		frame.getContentPane().add(buttonsPanel);
		
		frame.pack();
		frame.setVisible(true);
	}
	
	public void playSingleCmd() {
		if(!ifFiltered)
		{
			selectProcesses();			
			ifFiltered = true;			
		}
		
		if(lineIndex < commands.size())
		{
			singleCmd = commands.get(lineIndex);
			countersAndCmdPanel.showSingleCmd(singleCmd);
			if(singleCmd.contains("PF"))
			{
				countersAndCmdPanel.incrementPageFaultCounter();
			}
			else if(singleCmd.contains("PR"))
			{
				countersAndCmdPanel.incrementPageReplacementCounter();
				pageReplacementCmds.add(singleCmd);				
			}
			else if(singleCmd.contains("GP"))
			{
				ramPanel.updateTable(singleCmd, pageReplacementCmds);
			}
			
			lineIndex++;
		}
		else
		{
			buttonsPanel.playButton.setEnabled(false);		
			buttonsPanel.playAllButton.setEnabled(false);
		}		
	}

	public void playAllCmds() {
		for(int i = lineIndex; i < commands.size() + 1; i++)
		{
			playSingleCmd();
		}
	}	

	private void selectProcesses() {			
		List<String> selectedProcesses = procPanel.procList.getSelectedValuesList();
		List<String> updatedSelectedProc = new ArrayList<String>();
		int cleanIndex = 0;
		
		procPanel.procList.setEnabled(false);
		if(selectedProcesses.size() < this.numProcesses)
		{			
			for(String processStr : selectedProcesses)
			{
				String str = processStr.replace("Process ", "P");
				updatedSelectedProc.add(str);
			}				
			
			for(int i = 0; i < commands.size(); i++)
			{				
				String cmd = commands.get(i);				
				
				if(cmd.contains("RC") || cmd.contains("PN")) {
					cleanIndex++;
					continue;
				}
				
				if(cmd.contains("GP"))
				{
					String procName = cmd.substring(cmd.indexOf(":") + 1, cmd.indexOf(" "));
					if(updatedSelectedProc.contains(procName))
					{
						cleanIndex = i + 1;						
					}
					else
					{
						while(cleanIndex != i + 1) 
						{
							commands.set(cleanIndex, commands.get(cleanIndex) + "$");
							cleanIndex++;
						}					
					}
				}
			}	
		}		
	}

	public List<String> getCommands() {
		return commands;
	}

	public void setCommands(List<String> commands) {
		this.commands = commands;
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

	public RAMTablePanel getRamPanel() {
		return ramPanel;
	}

	public void setRamPanel(RAMTablePanel ramPanel) {
		this.ramPanel = ramPanel;
	}

	public CountersAndCommandsPanel getCountersPanel() {
		return countersAndCmdPanel;
	}

	public void setCountersPanel(CountersAndCommandsPanel countersPanel) {
		this.countersAndCmdPanel = countersPanel;
	}

	public ProcessesPanel getProcPanel() {
		return procPanel;
	}

	public void setProcPanel(ProcessesPanel procPanel) {
		this.procPanel = procPanel;
	}

	public ButtonsPanel getButtonsPanel() {
		return buttonsPanel;
	}

	public void setButtonsPanel(ButtonsPanel buttonsPanel) {
		this.buttonsPanel = buttonsPanel;
	}

	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}	
}
