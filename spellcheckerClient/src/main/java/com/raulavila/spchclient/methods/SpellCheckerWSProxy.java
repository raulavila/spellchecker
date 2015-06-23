package com.raulavila.spchclient.methods;

import com.raulavila.http.HttpClient;
import com.raulavila.http.constants.HttpConstants;
import com.raulavila.spchclient.exceptions.ServerErrorException;

public class SpellCheckerWSProxy implements SpellCheckerProxy {

    private static final String SPELL_CHECKER_URL_BASE = "http://localhost:4567";
    private static final String CHECK_SERVICE = "check";
    private static final String ADD_SERVICE = "add";

    private static final String EXCEPTION_SERVER_ERROR = "There has been an error in the server";

    private final HttpClient httpClient = HttpClient.getInstance();

    @Override
    public boolean check(String language, String word) {
        String url = buildUrl(CHECK_SERVICE, language, word);

        HttpClient.Response response = httpClient.getRequest(url);

        int status = response.getResponseStatus();

        if (status != HttpConstants.HTTP_STATUS_OK && status != HttpConstants.HTTP_STATUS_NOT_FOUND)
            throw new ServerErrorException(EXCEPTION_SERVER_ERROR);

        return status == HttpConstants.HTTP_STATUS_OK;
    }

    @Override
    public boolean add(String language, String word) {
        String url = buildUrl(ADD_SERVICE, language, word);

        HttpClient.Response response = httpClient.postRequest(url);

        int status = response.getResponseStatus();

        if (status != HttpConstants.HTTP_STATUS_OK && status != HttpConstants.HTTP_STATUS_CONFLICT)
            throw new ServerErrorException(EXCEPTION_SERVER_ERROR);

        //There hasn't been any error in the call (we don't mind if the word was previously added or not)
        return status == HttpConstants.HTTP_STATUS_OK;

    }

    private String buildUrl(String service, String language, String word) {
        StringBuilder url = new StringBuilder();

        url.append(SPELL_CHECKER_URL_BASE);
        url.append("/");

        url.append(service);
        url.append("/");

        url.append(language);
        url.append("/");

        url.append(word);

        return url.toString();
    }

}
