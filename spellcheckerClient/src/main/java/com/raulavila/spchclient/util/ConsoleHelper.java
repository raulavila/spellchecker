package com.raulavila.spchclient.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleHelper {

	public static File readValidFileName() {
		
		File file = null;
		boolean correctFile = false;
		
		while(!correctFile) {
			String fileName = readNonEmptyString();
			
			file = new File(fileName);
			
			if(file.exists() && file.isFile())
				correctFile = true;
			else
				System.out.println("Incorrect file! Please try again...");
		}
		
		return file;
	}
	
	public static String readNonEmptyString() {
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		String input = "";

		boolean correct = false;
		
		while(!correct)
		{
			try {
				input = br.readLine();
				
				if(input.isEmpty())
					System.out.println("Empty string, please try again...");
				else
					correct = true;
				
			} catch (IOException ioe) {
				System.out.println("IO error trying to read from console!");
				System.exit(1);
			}
		}
		
		return input;
	}
	
	public static String readYesNo() {
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		String input = "";

		boolean correct = false;
		
		while(!correct)
		{
			try {
				input = br.readLine();
				
				if("y".equals(input.toLowerCase()) || "n".equals(input.toLowerCase()))
					correct = true;
				else
					System.out.println("Incorrect answer, try again...");
				
			} catch (IOException ioe) {
				System.out.println("IO error trying to read from console!");
				System.exit(1);
			}
		}
		
		return input;
	}
	
}
