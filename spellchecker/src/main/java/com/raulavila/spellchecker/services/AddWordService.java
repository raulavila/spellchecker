package com.raulavila.spellchecker.services;

import com.raulavila.http.constants.HttpConstants;
import com.raulavila.spellchecker.exceptions.UnsupportedDictionaryException;
import com.raulavila.spellchecker.methods.SpellChecker;
import spark.Response;

public class AddWordService extends SpellCheckerService {

    private static final String WORD_ADDED_MESSAGE = "Word '%s (%s)' added to the repository";
    private static final String WORD_EXISTED_MESSAGE = "Word '%s (%s)' already existed in the repository";

    private final SpellChecker spellChecker;

    public AddWordService(SpellChecker spellChecker) {
        super("/add/:language/:word");
        this.spellChecker = spellChecker;
    }

    @Override
    protected String doHandle(String language,
                              String word,
                              Response response) throws UnsupportedDictionaryException {

        String responseBody;

        if (spellChecker.add(language, word)) {
            response.status(HttpConstants.HTTP_STATUS_OK);
            responseBody = getSimpleJson(ERROR_KEY, String.format(WORD_ADDED_MESSAGE, word, language));
        } else {
            response.status(HttpConstants.HTTP_STATUS_CONFLICT);
            responseBody = getSimpleJson(ERROR_KEY, String.format(WORD_EXISTED_MESSAGE, word, language));
        }

        return responseBody;
    }
}
