package com.raulavila.spellchecker.server;

import com.raulavila.spellchecker.repositories.WordRepository;
import com.raulavila.spellchecker.repositories.WordRepositoryImpl;
import com.raulavila.spellchecker.services.SpellCheckerServices;
import com.raulavila.spellchecker.methods.SpellChecker;
import com.raulavila.spellchecker.methods.SpellCheckerWR;
import com.raulavila.spellchecker.util.Validator;

public class SpellCheckerServer {

	public static void main(String[] args)  {
		
		Validator validator = new Validator();
		
		WordRepository wordRepository = new WordRepositoryImpl(validator);
		SpellChecker spellchecker = new SpellCheckerWR(wordRepository, validator);
		
		SpellCheckerServices.exposeServices(spellchecker);
	}

}
