package com.hit.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ButtonsPanel extends JPanel implements ActionListener {
	protected MMUView view;	
	protected JButton playButton;
	protected JButton playAllButton;
	private JPanel subPanel;
	
	public ButtonsPanel(MMUView view) {
		subPanel = new JPanel();
		this.view = view;
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		setLayout(new BorderLayout());		
		playButton = new JButton("PLAY");
		playButton.setActionCommand("PLAY");
		playButton.addActionListener(this);		
		playAllButton = new JButton("PLAY ALL");
		playAllButton.setActionCommand("PLAY ALL");
		playAllButton.addActionListener(this);
		subPanel.add(playButton);
		subPanel.add(playAllButton); 
		add(subPanel, BorderLayout.SOUTH);
		this.setBackground(Color.WHITE);
		subPanel.setBackground(Color.WHITE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {		
		if(e.getActionCommand() == playButton.getActionCommand()){
			view.playSingleCmd();
		}
		else if(e.getActionCommand() == playAllButton.getActionCommand()){
			view.playAllCmds();
		}
	}
	
}
