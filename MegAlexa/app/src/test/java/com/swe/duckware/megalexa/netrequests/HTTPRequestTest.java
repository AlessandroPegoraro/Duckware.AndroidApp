package com.swe.duckware.megalexa.netrequests;

import org.junit.Test;
import static org.junit.Assert.*;

public class HTTPRequestTest {

    @Test
    public void http_responseBody() {
        assertEquals("test", HTTPRequest.extractResponseBody("{\"statusCode\": 0,\"userData\":\"test\"}"));
    }

    @Test
    public void http_responseCode() {
        assertEquals(0, HTTPRequest.extractResponseCode("{\"statusCode\": 0,\"userData\":\"test\"}"));
    }

}