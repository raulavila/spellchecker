package com.raulavila.spellchecker.services;

import com.google.gson.JsonObject;
import com.raulavila.http.constants.HttpConstants;
import com.raulavila.spellchecker.exceptions.MissingArgumentException;
import com.raulavila.spellchecker.exceptions.UnsupportedDictionaryException;
import spark.Request;
import spark.Response;
import spark.Route;

import java.net.URLDecoder;

public abstract class SpellCheckerService extends Route {

    protected static final String UTF_8_ENCODING = "UTF-8";
    protected static final String ERROR_KEY = "Error";
    public static final String INTERNAL_ERROR_MESSAGE = "There has been an internal error on the server";

    public SpellCheckerService(String route) {
        super(route);
    }

    protected static String getSimpleJson(String key, String value) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(key, value);

        return jsonObject.toString();
    }

    @Override
    public Object handle(Request request, Response response) {
        String responseBody;
        try {
            String language = URLDecoder.decode(request.params(":language"), UTF_8_ENCODING);
            String word = URLDecoder.decode(request.params(":word"), UTF_8_ENCODING);

            responseBody = doHandle(language, word, response);
        } catch (UnsupportedDictionaryException | MissingArgumentException e) {
            response.status(HttpConstants.HTTP_STATUS_INCORRECT_REQUEST);
            responseBody = getSimpleJson(ERROR_KEY, e.getMessage());

        } catch (Exception e) {
            e.printStackTrace();

            response.status(HttpConstants.HTTP_STATUS_INTERNAL_ERROR);
            responseBody = getSimpleJson(ERROR_KEY, INTERNAL_ERROR_MESSAGE);
        }

        response.type(HttpConstants.ACCEPT_HEADER_JSON + HttpConstants.RESPONSE_CHARSET_UTF8);
        return responseBody;

    }

    protected abstract String doHandle(String language,
                                       String word,
                                       Response response) throws UnsupportedDictionaryException;
}
