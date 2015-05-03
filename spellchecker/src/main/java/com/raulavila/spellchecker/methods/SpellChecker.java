package com.raulavila.spellchecker.methods;

import com.raulavila.spellchecker.exceptions.UnsupportedDictionaryException;

public interface SpellChecker {
	
	boolean check(String language, String word) throws UnsupportedDictionaryException;
	
	public boolean add(String language, String word) throws UnsupportedDictionaryException ;

}
