package com.hit.util;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class MMULogger {
	public final static String DEFAULT_FILE_NAME = "logs/log.txt";
	private FileHandler handler;
	private static MMULogger mmuLogger;	
	
	private MMULogger() {
		File file = new File(DEFAULT_FILE_NAME);
		
		if(file.exists()){
			file.delete();
		}
		
		try {
			handler = new FileHandler(DEFAULT_FILE_NAME);
			handler.setFormatter(new OnlyMessageFormatter());
		} catch (SecurityException | IOException e) {
			System.out.println(e);
			e.printStackTrace();
		}
	}
	
	public static MMULogger getInstance(){
		if(mmuLogger == null)
		{
			mmuLogger = new MMULogger();
		}
		
		return mmuLogger;
	}
	
	public synchronized void write(String command, Level level){
		handler.publish(new LogRecord(level, command));
	}
	
	public class OnlyMessageFormatter extends Formatter	{
		public OnlyMessageFormatter(){
			super();
		}

		@Override
		public String format(final LogRecord record) {
			return record.getMessage();
		}		
	}
	
	public FileHandler getHandler() {
		return handler;
	}

	public void setHandler(FileHandler handler) {
		this.handler = handler;
	}

}
