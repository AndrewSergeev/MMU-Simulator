package com.hit.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

@SuppressWarnings("serial")
public class ProcessesPanel extends JPanel {
	protected MMUView view;	
	protected JLabel procLabel;
	protected JList<String> procList;
	protected JScrollPane scrollPane;
	
	public ProcessesPanel(MMUView view) {		
		this.view = view;
		String[] procNames = new String[view.getNumProcesses()];
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		for(int i = 0; i < view.getNumProcesses(); i++)
		{
			procNames[i] = String.format("Process %d", i+1);
		}
		
		setLayout(new BorderLayout());
		procLabel = new JLabel("Processes");
		procLabel.setFont(new Font(procLabel.getFont().getName(), procLabel.getFont().getStyle(), 16));
		procList = new JList<String>(procNames);
		procList.addSelectionInterval(0, view.getNumProcesses() - 1);
		procList.setFont(new Font(procList.getFont().getName(), procList.getFont().getStyle(), 16));
		setPreferredSize(new Dimension(40, 60));
		procList.setSize(40, 50);
		add(procLabel, BorderLayout.NORTH);
		scrollPane = new JScrollPane(procList);
		add(scrollPane, BorderLayout.CENTER);
		this.setBackground(Color.WHITE);
	}
}
