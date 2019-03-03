package com.swe.duckware.megalexa.netrequests;

import android.support.v7.app.AppCompatActivity;
import android.util.JsonReader;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

public class HTTPRequest {

    private static String BASE_URL = "https://voa6w60va3.execute-api.eu-west-1.amazonaws.com/";

    private static String LOGIN_PATH = "MegAlexa/login?";

    private static String REGISTER_PATH = "MegAlexa/register?";

    private static String WORKFLOW_ADD_PATH = "MegAlexa/workflowadd?";

    private static String WORKFLOW_REMOVE_PATH = "MegAlexa/workflowremove?";

    private Map<String, String> params;

    private HTTPRequestAction action;

    private String buildParams() {
        StringBuilder sb = new StringBuilder();

        for(Map.Entry<String, String> e : params.entrySet()) {
            sb.append(e.getKey()).append("=").append(e.getValue()).append("&");
        }

        //Remove the last useless '&' char
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1);
        }

        return sb.toString();
    }

    private String buildPath() {
        String path = "";

        switch (action) {
            case Register:
                path = BASE_URL + REGISTER_PATH + buildParams();
                break;
            case Login:
                path = BASE_URL + LOGIN_PATH + buildParams();
                break;
            case WorkflowAdd:
                path = BASE_URL + WORKFLOW_ADD_PATH + buildParams();
                break;
            case WorkflowDelete:
                path = BASE_URL + WORKFLOW_REMOVE_PATH + buildParams();
        }

        return path;
    }

    public HTTPRequest(Map<String, String> params, HTTPRequestAction action) {
        this.params = params;
        this.action = action;
    }

    public void doRequest(AppCompatActivity context,
                          HTTPCompletionHandler completionHandler,
                          HTTPErrorHandler errorHandler) {
        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest httpRequest = new StringRequest(Request.Method.GET, buildPath(),
                (response) -> completionHandler.onCompleted(response),
                (error) -> errorHandler.onError(error.getMessage()) ) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("x-api-key", "WpfEWsfTAF6BxOfZbLxzUWmy6uPElnC1B5QhDzua");
                return params;
            }

        };

        queue.add(httpRequest);
    }

    public static int extractResponseCode(String jsonResponse) {
        int responseCode = 0;
        JsonReader reader = new JsonReader(new StringReader(jsonResponse));

        try {
            reader.beginObject();

            while (reader.hasNext()) {
                String name = reader.nextName();

                if (name.equals("statusCode")) {
                    responseCode = reader.nextInt();
                } else {
                    reader.skipValue();
                }
            }

        } catch (IOException exc) {
            responseCode = -1;
        }

        return responseCode;
    }

    public static String extractResponseBody(String jsonResponse) {
        String responseBody = "";
        JsonReader reader = new JsonReader(new StringReader(jsonResponse));

        try {
            reader.beginObject();

            while (reader.hasNext()) {
                String name = reader.nextName();

                if (name.equals("userData")) {
                    responseBody = reader.nextString();
                } else {
                    reader.skipValue();
                }
            }

        } catch (IOException exc) {
            responseBody = "-";
        }

        return responseBody;
    }

}