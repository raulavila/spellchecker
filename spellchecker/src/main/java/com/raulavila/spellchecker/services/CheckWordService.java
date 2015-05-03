package com.raulavila.spellchecker.services;

import com.raulavila.http.constants.HttpConstants;
import com.raulavila.spellchecker.exceptions.UnsupportedDictionaryException;
import com.raulavila.spellchecker.methods.SpellChecker;
import spark.Response;

public class CheckWordService extends SpellCheckerService {

	private static final String VALID_WORD_MESSAGE = "Valid word: '%s (%s)'";
	private static final String INVALID_WORD_MESSAGE = "Invalid word: '%s (%s)'";
	
	private final SpellChecker spellChecker;
	
	public CheckWordService(SpellChecker spellChecker) {
		super("/check/:language/:word");
		this.spellChecker = spellChecker;
	}

	@Override
	protected String doHandle(String language, 
							  String word, 
							  Response response) throws UnsupportedDictionaryException {

		String responseBody;
		
		if(spellChecker.check(language, word)) {
			response.status(HttpConstants.HTTP_STATUS_OK);
			responseBody = getSimpleJson(ERROR_KEY, String.format(VALID_WORD_MESSAGE, word, language));
		}
		else {
			response.status(HttpConstants.HTTP_STATUS_NOT_FOUND);
			responseBody = getSimpleJson(ERROR_KEY, String.format(INVALID_WORD_MESSAGE, word, language));
		}

		return responseBody;
	}
}
