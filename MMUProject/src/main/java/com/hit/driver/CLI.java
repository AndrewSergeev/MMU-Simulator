package com.hit.driver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Observable;
import java.util.Scanner;

//import com.hit.model.MMUModel;
import com.hit.view.View;

public class CLI extends Observable implements Runnable, View{
	public static final String START = "start";
	public static final String STOP = "stop";
	public static final String LRU = "LRU";
	public static final String MRU = "MRU";
	public static final String Random = "Random";
	private BufferedReader in;
	private BufferedWriter out;
	private String command[];

	public CLI(InputStream in, OutputStream out){
		this.in = new BufferedReader(new InputStreamReader(in));
		this.out = new BufferedWriter(new OutputStreamWriter(out));
	}	

	@Override
	public void run() {
		Scanner scanner = new Scanner(in);	
		String input;		
		boolean running = true;			

		while(running){
			write("Please type 'start' for turn on the system or 'stop' to get out: ");
			input = scanner.nextLine();
			if(input.equalsIgnoreCase(START))
			{
				write("Please enter required algorithm and RAM capacity: ");
				input = scanner.nextLine();
				command = input.split(" ");
				while(!checkIfValidSize(command, 2) )
				{
					write("Not a valid command. Try again: ");
					input = scanner.nextLine();	
					command = input.split(" ");
				}

				if((command[0].equalsIgnoreCase(LRU) || command[0].equalsIgnoreCase(MRU) || 
						command[0].equalsIgnoreCase(Random)) && Integer.parseInt(command[1]) > 0)
				{
					write("Loading...\n");
					setChanged();
					this.notifyObservers(command);					
					command = null;					
				}
				else
				{
					write("Not a valid command. ");
				}
			}
			else if (input.equalsIgnoreCase(STOP))
			{
				running = false;
				write("Thank you");	
			}
			else
			{
				write("Not a valid command. ");				
			}
		}

		scanner.close();
	}	


	private boolean checkIfValidSize(String[] stringArray, int size) {
		boolean ifValidSize = true;
		
		if(stringArray.length != size)
		{
			ifValidSize = false;
		}
		
		return ifValidSize;
	}

	public void write(String string){		
		try {
			out.write(string);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String[] getCommand() {
		return command;
	}

	@Override
	public void start() {}
}
