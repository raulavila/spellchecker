package com.raulavila.spellchecker.services;

import spark.Spark;

import com.raulavila.spellchecker.methods.SpellChecker;

public class SpellCheckerServices {

	public static void exposeServices(SpellChecker spellChecker) {
		exposeCheckWord(spellChecker);
		exposeAddWord(spellChecker);
	}
	
	private static void exposeCheckWord(SpellChecker spellChecker) {
		Spark.get(new CheckWordService(spellChecker));
		
	}
	
	private static void exposeAddWord(final SpellChecker spellChecker) {
		Spark.post(new AddWordService(spellChecker));
	}
	
}
