package com.raulavila.spellchecker.repositories;

import com.raulavila.spellchecker.util.Validator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WordRepositoryImpl implements WordRepository {
	
	private final Map<String, Set<String>> repository = new HashMap<String, Set<String>>();

	private final Validator validator;

	public WordRepositoryImpl(Validator validator) {
		this.validator = validator;
	}

	@Override
	public boolean addWord(String language, String word) {
		checkParams(language, word);
		
		boolean isNewWord = true;
		
		synchronized(repository) {
			if(repository.containsKey(language)) {
				isNewWord = repository.get(language).add(word);
			}
			else {
				Set<String> wordSet = new HashSet<String>();
				wordSet.add(word);
				repository.put(language, wordSet);
			}
		}
		
		return isNewWord;
	}

	@Override
	public boolean removeWord(String language, String word) {
		checkParams(language, word);

		boolean existed = false;

		synchronized(repository) {
			if(repository.containsKey(language)) {
				existed = repository.get(language).remove(word);
			}
		}

		return existed;
	}

	@Override
	public boolean contains(String language, String word) {
		checkParams(language, word);

		boolean exists = false;

		synchronized(repository) {
			if(repository.containsKey(language)) {
				exists = repository.get(language).contains(word);
			}
		}

		return exists;
	}

	private void checkParams(String language, String word) {
		validator.validateParam("language", language);
		validator.validateParam("word", word);
	}

}
