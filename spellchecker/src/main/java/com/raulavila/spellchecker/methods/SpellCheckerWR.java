package com.raulavila.spellchecker.methods;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.raulavila.http.HttpClient;
import com.raulavila.spellchecker.exceptions.UnsupportedDictionaryException;
import com.raulavila.spellchecker.repositories.WordRepository;
import com.raulavila.spellchecker.util.Validator;

public class SpellCheckerWR implements SpellChecker {

    // WordReference
    public static final String WR_URL_ROOT = "http://api.wordreference.com";
    public static final String WR_API_VERSION = "0.8";
    public static final String WR_API_KEY = "00932";
    public static final String WR_JSON_REQUEST = "json";
    public static final String WR_DICTIONARY_DESTINY = "es";
    public static final String WR_DICTIONARY_DESTINY_ALT = "fr";

    public static final String WR_UNSUPPORTED_DICTIONARY_MESSAGE = "UnsupportedDictionary";

    public static final String WR_NO_TRANSLATION_MESSAGE = "NoTranslation";

    public static final String WR_JSON_ERROR_KEY = "Error";
    public static final String WR_JSON_RESPONSE_KEY = "Response";
    public static final String WR_JSON_RESPONSE_REDIRECT_MESSAGE = "Redirect";

    // Exceptions
    public static final String EXCEPTION_UNSUPPORTED_DICTIONARY = "The language specified in the request is not valid";
    public static final String ERROR_WR_API_CHANGED = "The WR API has changed!!!";
    public static final String EXCEPTION_UNKNOWN_WR_ERROR = "An unknown WR error has been produced: %s";


    private final HttpClient httpClient = HttpClient.getInstance();
    private final JsonParser jsonParser = new JsonParser();

    private final WordRepository wordRepository;
    private final Validator validator;

    public SpellCheckerWR(WordRepository wordRepository, Validator validator) {
        this.wordRepository = wordRepository;
        this.validator = validator;
    }

    @Override
    public boolean check(String language, String word) throws UnsupportedDictionaryException {
        checkParams(language, word);

        String url = buildWRRequest(language, word);
        String json = httpClient.getJson(url);

        return validWordReferenceWord(json) ||
                wordRepository.contains(language, word);
    }

    private String buildWRRequest(String language, String word) {
        StringBuilder url = buildUrlBase(language);
        url.append(word);
        return url.toString();
    }

    private boolean validWordReferenceWord(String json) throws UnsupportedDictionaryException {

        JsonElement jelement = jsonParser.parse(json);
        JsonObject jobject = jelement.getAsJsonObject();

        if (jobject.has(WR_JSON_ERROR_KEY))
            return processError(jobject);
        else if (jobject.has(WR_JSON_RESPONSE_KEY))
            return processRedirect(jobject);
        else if (!jobject.isJsonNull())   //Valid word
            return true;
        else
            throw new AssertionError(ERROR_WR_API_CHANGED);   //The WR API may have changed

    }

    private boolean processError(JsonObject jobject) throws UnsupportedDictionaryException {
        JsonElement error = jobject.get(WR_JSON_ERROR_KEY);

        String errorMessage = error.getAsString();

        //In this case the word does not exist
        if (errorMessage.equals(WR_NO_TRANSLATION_MESSAGE))
            return false;
        else if (errorMessage.equals(WR_UNSUPPORTED_DICTIONARY_MESSAGE))
            throw new UnsupportedDictionaryException(EXCEPTION_UNSUPPORTED_DICTIONARY);
        else
            throw new RuntimeException(String.format(EXCEPTION_UNKNOWN_WR_ERROR, errorMessage));
    }

    private boolean processRedirect(JsonObject jobject) {
        JsonElement response = jobject.get(WR_JSON_RESPONSE_KEY);

        String responseMessage = response.getAsString();

        //In this case the word does not exist, WR is trying to redirect to other dictionary
        if (responseMessage.equals(WR_JSON_RESPONSE_REDIRECT_MESSAGE))
            return false;
        else
            throw new RuntimeException(String.format(EXCEPTION_UNKNOWN_WR_ERROR, responseMessage));
    }

    @Override
    public boolean add(String language, String word) throws UnsupportedDictionaryException {
        checkParams(language, word);

        //It doesn't add the word if it's in WordReference
        if (check(language, word))
            return false;
        else
            return wordRepository.addWord(language, word);
    }


    private void checkParams(String language, String word) {
        validator.validateParam("language", language);
        validator.validateParam("word", word);
    }


    private StringBuilder buildUrlBase(String language) {
        StringBuilder url = new StringBuilder();

        url.append(WR_URL_ROOT);
        url.append("/");

        url.append(WR_API_VERSION);
        url.append("/");

        url.append(WR_API_KEY);
        url.append("/");

        url.append(WR_JSON_REQUEST);
        url.append("/");

        url.append(language);
        url.append(language.equals(WR_DICTIONARY_DESTINY) ? WR_DICTIONARY_DESTINY_ALT : WR_DICTIONARY_DESTINY);
        url.append("/");

        return url;
    }


}
