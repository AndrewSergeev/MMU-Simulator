package com.hit.controller;

import java.util.List;
import java.util.Observable;
import java.util.logging.Level;

import com.hit.driver.CLI;
import com.hit.model.MMUModel;
import com.hit.model.Model;
import com.hit.util.MMULogger;
import com.hit.view.MMUView;
import com.hit.view.View;

public class MMUController implements Controller {
	private Model model;
	private View view;
	
	public MMUController(MMUModel model, MMUView view) {
		this.model = model;
		this.view = view;
	}	

	@Override
	public void update(Observable o, Object arg) {
		if(o instanceof CLI){
			try {
				model.start((String[]) arg);
			} catch (InterruptedException e) {
				MMULogger.getInstance().write(e.getMessage(), Level.SEVERE);
				e.printStackTrace();
			}
		}
		else if(o instanceof MMUModel){
			MMULogger.getInstance().getHandler().close();
			view.start();///check it
		}
		else if(o instanceof MMUView){
			List<String> commands = ((MMUModel) model).getCommandsFromLogFile();
			int ramCapacity = ((MMUModel) model).getRamCapacity();
			int numOfProc = ((MMUModel) model).getNumProcesses();
			
			((MMUView) view).setCommands(commands);
			((MMUView) view).setNumProcesses(numOfProc);
			((MMUView) view).setRamCapacity(ramCapacity);
		}		
	}
}
