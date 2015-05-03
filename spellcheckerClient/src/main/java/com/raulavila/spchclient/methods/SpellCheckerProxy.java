package com.raulavila.spchclient.methods;

public interface SpellCheckerProxy {

	boolean check(String language, String word);
	
	boolean add(String language, String word);
	
}
