package com.raulavila.http;

import com.raulavila.http.constants.HttpConstants;
import com.raulavila.http.exceptions.HttpClientException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * HttpClient, JSON oriented
 *
 * @author Raul Avila
 */
public class HttpClient {

    private static final int MAX_CONNECTIONS = 100;

    public static final String EXCEPTION_PROTOCOL_ERROR = "Unexpected error trying to get URL %s";
    public static final String EXCEPTION_FAILED_REQUEST = "Failed request : HTTP error code : %d";

    public static final HttpClient INSTANCE = new HttpClient();

    private final CloseableHttpClient httpClient;

    public static HttpClient getInstance() {
        return INSTANCE;
    }

    private HttpClient() {
        PoolingHttpClientConnectionManager connectionManager
                = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(MAX_CONNECTIONS);

        httpClient = HttpClients
                .custom()
                .setConnectionManager(connectionManager)
                .build();
    }

    /**
     * Returns the json resource mapped by url. This method expects the status response to be 200 (OK)
     *
     * @param url url to get json object
     * @return json object
     * @throws HttpClientException If the response code is not OK (200), or there has been any error
     */
    public String getJson(String url) {
        CloseableHttpResponse httpResponse = null;

        try {
            HttpContext context = HttpClientContext.create();
            HttpGet getRequest = new HttpGet(url);

            httpResponse = httpClient.execute(getRequest, context);
            validateHttpResponse(httpResponse);

            return buildJsonResponse(httpResponse);
        } catch (Exception e) {
            throw new HttpClientException(String.format(EXCEPTION_PROTOCOL_ERROR, url), e);
        } finally {
            closeHttpResponse(httpResponse);
        }
    }

    private void validateHttpResponse(CloseableHttpResponse httpResponse) {
        if (httpResponse.getStatusLine().getStatusCode() != HttpConstants.HTTP_STATUS_OK) {
            throw new HttpClientException(String.format(EXCEPTION_FAILED_REQUEST,
                    httpResponse.getStatusLine().getStatusCode()));
        }
    }

    private String buildJsonResponse(CloseableHttpResponse httpResponse) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader((httpResponse.getEntity().getContent())));

        StringBuilder output = new StringBuilder();

        String line;
        while ((line = br.readLine()) != null)
            output.append(line);

        return output.toString();
    }

    /**
     * Sends a post request to the specified url
     *
     * @param url
     * @return HttpClient.Response object, containing response status, and response body, if any
     * @throws HttpClientException If there has been any error
     */
    public Response postRequest(String url) {
        CloseableHttpResponse httpResponse = null;

        try {
            HttpContext context = HttpClientContext.create();

            HttpPost postRequest = new HttpPost(url);
            postRequest.addHeader(HttpConstants.ACCEPT_HEADER_KEY, HttpConstants.ACCEPT_HEADER_JSON);

            httpResponse = httpClient.execute(postRequest, context);

            return buildResponse(httpResponse);

        } catch (Exception e) {
            throw new HttpClientException(String.format(EXCEPTION_PROTOCOL_ERROR, url), e);
        } finally {
            closeHttpResponse(httpResponse);
        }

    }

    /**
     * Sends a get request to the specified url
     *
     * @param url
     * @return HttpClient.Response object, containing response status, and response body, if any
     * @throws HttpClientException If there has been any error
     */
    public Response getRequest(String url) {

        CloseableHttpResponse httpResponse = null;

        try {
            HttpContext context = HttpClientContext.create();

            HttpGet getRequest = new HttpGet(url);
            getRequest.addHeader(HttpConstants.ACCEPT_HEADER_KEY, HttpConstants.ACCEPT_HEADER_JSON);

            httpResponse = httpClient.execute(getRequest, context);

            return buildResponse(httpResponse);

        } catch (Exception e) {
            throw new HttpClientException(String.format(EXCEPTION_PROTOCOL_ERROR, url), e);
        } finally {
            closeHttpResponse(httpResponse);
        }

    }

    private Response buildResponse(CloseableHttpResponse httpResponse) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader((httpResponse.getEntity().getContent())));

        StringBuilder output = new StringBuilder();

        String line;
        while ((line = br.readLine()) != null)
            output.append(line);


        return new Response(httpResponse.getStatusLine().getStatusCode(), output.toString());
    }


    private void closeHttpResponse(CloseableHttpResponse httpResponse) {
        try {
            if (httpResponse != null)
                httpResponse.close();
        } catch (Exception e) {
            System.out.println("Error closing httpResponse");
        }
    }

    protected void finalize() throws Throwable {
        try {
            httpClient.close();
        } finally {
            super.finalize();
        }
    }


    public static class Response {
        private final int responseStatus;
        private final String responseBody;

        public Response(int responseStatus, String responseBody) {
            this.responseStatus = responseStatus;
            this.responseBody = responseBody;
        }

        public int getResponseStatus() {
            return responseStatus;
        }

        public String getResponseBody() {
            return responseBody;
        }

        @Override
        public String toString() {
            return "Response [responseStatus=" + responseStatus
                    + ", responseBody=" + responseBody + "]";
        }

    }

}