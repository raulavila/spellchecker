package com.raulavila.spchclient.client;

import com.raulavila.spchclient.exceptions.ServerErrorException;
import com.raulavila.spchclient.methods.SpellCheckerProxy;
import com.raulavila.spchclient.methods.SpellCheckerWSProxy;
import com.raulavila.spchclient.util.ConsoleHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class SpellCheckerClient {

	private static final String LANGUAGE = "en";

	private static Set<String> processFile(File file, SpellCheckerProxy spellChecker) {
		
		//Set to minimize requests to the server
		Set<String> processedWords = new HashSet<String>();
		
		Set<String> invalidWords = new HashSet<String>();
		
		try {
			Scanner scanner = new Scanner(file);
			while (scanner.hasNext()) {
				
				//Remove all non-alphabetical characters, except hyphens and apostrophes
				String word = scanner.next().replaceAll("[^\\w-']", "");

				if(!processedWords.contains(word)) {
					
					if(!spellChecker.check(LANGUAGE, word)) {
						System.out.print("The word '"+word+"' does not exist, do you want to add it to the server repository (Y/N)? ");

						String answer = ConsoleHelper.readYesNo();
						
						if("y".equals(answer.toLowerCase()))
							spellChecker.add(LANGUAGE, word);  //We don't need to know if the word already existed in the repo
						else   //If the user chooses not to add the word, the word wasn't registered in the server, and it's invalid
							invalidWords.add(word);  
					}
					processedWords.add(word);
				}
			}
			
			scanner.close();
			
			return invalidWords;
		} 
		catch (FileNotFoundException e) {
			System.out.println("IO error trying to open the file");
			System.exit(1);
			return null;
		}
	}

	public static void main(String[] args) {

		System.out.println("Welcome to Spell Checker client");
		
		System.out.println("Please enter the complete path of the file to process: ");

		File file = ConsoleHelper.readValidFileName();
		
		SpellCheckerProxy spellChecker = new SpellCheckerWSProxy();
		
		try { 
		
			System.out.println("Processing file...");
			Set<String> invalidWords = processFile(file, spellChecker);
			
			if(invalidWords.isEmpty())
				System.out.println("The file does not contain invalid words");
			else {
				Set<String> invalidWordsOrdered = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
				invalidWordsOrdered.addAll(invalidWords);
				
				System.out.println(String.format("The file contains %d invalid words. In alphabetical order: %s",
												invalidWordsOrdered.size(),
												invalidWordsOrdered.toString()));
			
			}
		}
		catch(ServerErrorException e) {
			System.out.println("There has been an error in the server!!");
			System.exit(1);
		}
		catch(Exception e) {
			System.out.println("Unexpected exception "+e.getMessage());
			System.exit(1);
		}
		
		System.out.println("Process finished!");

	}

}
