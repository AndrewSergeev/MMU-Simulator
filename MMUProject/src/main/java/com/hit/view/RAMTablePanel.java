package com.hit.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

@SuppressWarnings("serial")
public class RAMTablePanel extends JPanel{
	protected MMUView view;	 
	protected JTable ramTable;
	protected JScrollPane scrollPane;
	protected String[][] tableContent;
	protected TableModel model;
	protected Integer numOfPgsInRam = 0;
	private RamTableRenderer ramTableRenderer;
	
	public RAMTablePanel(MMUView mmuView) {
		view = mmuView;
		ramTableRenderer = new RamTableRenderer();
		
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));		
		setLayout(new BorderLayout());
		String[] indexies = new String[view.getRamCapacity()];
		for(Integer i = 1; i <= view.getRamCapacity(); i++)
		{
			indexies[i - 1] = i.toString();
		}
		
		ramTable = new JTable(new DefaultTableModel(new String[6][view.getRamCapacity()], indexies));
		if(view.getRamCapacity() > 5)
		{
			 setPreferredSize(new Dimension(400, 155));
			 ramTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		} 
		else
		{
			setPreferredSize(new Dimension((int) ramTable.getSize().getWidth(), 140));
		}
		
        scrollPane = new JScrollPane(ramTable);
        ramTable.setEnabled(false);
        add(scrollPane);
        ramTable.setDefaultRenderer(Object.class, ramTableRenderer);
        this.setBackground(Color.WHITE);
	}

	public void updateTable(String singleCmd, List<String> pageReplacementCmds) {
		String pageId = singleCmd.substring(singleCmd.indexOf(" ") + 1, singleCmd.indexOf("[") - 1);
		String[] content = singleCmd.substring(singleCmd.indexOf("[") + 1, singleCmd.indexOf("]")).split(",");
		boolean visible = true;
		
		if(singleCmd.contains("$")) {
			visible = false;
		}			
		
		if(!pageReplacementCmds.isEmpty() && numOfPgsInRam >= view.getRamCapacity())
		{	
			String pageReplacementCmd = pageReplacementCmds.get(0);
			String pageToHd = pageReplacementCmd.substring(pageReplacementCmd.indexOf("H") + 2, pageReplacementCmd.indexOf("MTR") - 1);
			removeFromTable(pageToHd, content.length);
			pageReplacementCmds.remove(0);
		}
		
		addToTable(pageId, content, visible);
	}
	
	public void removeFromTable(String pageToHd, int length)
	{
		numOfPgsInRam--;		
		for(int col = 0; col < view.getRamCapacity(); col++)
		{
			String valueId = (String) ramTable.getValueAt(0, col);
			
			if(valueId != null)
			{
				if(valueId.equals("id: " + pageToHd) || valueId.equals("id: " + pageToHd + "$"))
				{
					for(int row = 0; row < length + 1; row++)
					{
						ramTable.setValueAt(null, row, col);
					}
					
					break;
				}
			}			
		}		
	}
	
	public void addToTable(String id, String[] content, boolean visible)
	{
		boolean ifExists = false;
		String visibilitySymbol = "";
		
		if(!visible)
		{
			visibilitySymbol = "$";
		}
		
		for(int col = 0; col < view.getRamCapacity(); col++)
		{
			String valueId = (String) ramTable.getValueAt(0, col);
			
			if(valueId != null)
			{
				if(valueId.equals("id: " + id) || 
						valueId.equals("id: " + id + "$") || valueId.equals("id: " + id + ""))
				{
					ifExists = true;
					for(int row = 1; row < content.length + 1; row++)
					{
						ramTable.setValueAt(content[row - 1] + visibilitySymbol, row, col);
					}
					
					break;
				}
			}			
		}		
		
		if(!ifExists)
		{
			numOfPgsInRam++;
			model = ramTable.getModel();
			for(int col = 0; col < view.getRamCapacity(); col++)
			{
				if(model.getValueAt(0, col) == null)   
				{
					ramTable.setValueAt("id: " + id + visibilitySymbol, 0, col);
					for(int row = 1; row < content.length + 1; row++)
					{
						ramTable.setValueAt(content[row - 1] + visibilitySymbol, row, col);
					}
					
					break;
				}
			}
		}		
	}
}









