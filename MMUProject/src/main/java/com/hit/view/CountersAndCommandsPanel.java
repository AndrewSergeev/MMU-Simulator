package com.hit.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class CountersAndCommandsPanel extends JPanel{
	protected MMUView view;	
	protected JLabel pfCounterLabel;
	protected JLabel prCounterLabel;
	protected JLabel cmdLabel;
	protected JTextField pfTextField;
	protected JTextField prTextField;
	protected JTextField cmdTextField;
	protected Integer pfCounter = 0;
	protected Integer prCounter = 0;
	
	public CountersAndCommandsPanel(MMUView mmuView) {
		view = mmuView;
		setLayout(new GridLayout(3,3));
		setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
		setBackground(Color.WHITE);
		
		pfCounterLabel = new JLabel("Page Fault Amount  ");
		pfCounterLabel.setFont(new Font(pfCounterLabel.getFont().getName(), pfCounterLabel.getFont().getStyle(), 16));
		add(pfCounterLabel);
		pfTextField = new JTextField(pfCounter.toString());
		pfTextField.setBackground(Color.white);
		pfTextField.setEditable(false);
		pfTextField.setFont(new Font(pfTextField.getFont().getName(), pfTextField.getFont().getStyle(), 16));
		pfTextField.setBorder(null);
		pfTextField.setHorizontalAlignment(JTextField.CENTER);
		add(pfTextField);
		
		prCounterLabel = new JLabel("Page Replacement Amount  ");
		prCounterLabel.setFont(new Font(prCounterLabel.getFont().getName(), prCounterLabel.getFont().getStyle(), 16));
		add(prCounterLabel);
		prTextField = new JTextField(prCounter.toString());
		prTextField.setBackground(Color.white);		
		prTextField.setEditable(false);
		prTextField.setFont(new Font(prTextField.getFont().getName(), prTextField.getFont().getStyle(), 16));
		prTextField.setBorder(null);
		prTextField.setHorizontalAlignment(JTextField.CENTER);
		add(prTextField);	
		
		cmdLabel = new JLabel("Current Command ");
		cmdLabel.setFont(new Font(cmdLabel.getFont().getName(), cmdLabel.getFont().getStyle(), 16));
		add(cmdLabel);
		cmdTextField = new JTextField("-");
		cmdTextField.setBackground(Color.white);
		cmdTextField.setEditable(false);
		cmdTextField.setFont(new Font(cmdTextField.getFont().getName(), cmdTextField.getFont().getStyle(), 14));
		cmdTextField.setBorder(null);
		cmdTextField.setHorizontalAlignment(JTextField.CENTER);
		add(cmdTextField);		
	}	
	
	public void incrementPageFaultCounter(){
		pfCounter++;
		pfTextField.setText((pfCounter).toString());
	}
	
	public void incrementPageReplacementCounter(){
		prCounter++;
		prTextField.setText((prCounter).toString());
	}

	public void showSingleCmd(String singleCmd) {
		if(singleCmd.contains("$"))
		{
			singleCmd = "Hidden command";
		}
		
		cmdTextField.setText(singleCmd);		
	}
}
