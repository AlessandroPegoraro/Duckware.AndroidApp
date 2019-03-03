package com.swe.duckware.megalexa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.swe.duckware.megalexa.netrequests.HTTPRequest;
import com.swe.duckware.megalexa.netrequests.HTTPRequestAction;
import com.swe.duckware.megalexa.netrequests.InputValidator;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class LoginActivity extends AppCompatActivity {

    private SharedPreferences prefs = null;

    private int responseCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        loadLastAccess();
    }

    public void doLogin(View view) {
        String username = ((EditText) findViewById(R.id.emailLogin)).getText().toString();
        String password = ((EditText) findViewById(R.id.passwordLogin)).getText().toString();

        InputValidator validator = new InputValidator(username, password);

        if (!validator.isEmailValid()) {
            Toast.makeText(getApplicationContext(), getString(R.string.invalid_email), Toast.LENGTH_LONG).show();
        } else {
            if (!validator.isPasswordValid()) {
                Toast.makeText(getApplicationContext(), getString(R.string.invalid_password), Toast.LENGTH_LONG).show();
            } else {
                //Show the progress bar
                findViewById(R.id.progressLogin).setVisibility(View.VISIBLE);

                //Build the request
                HashMap<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);

                new HTTPRequest(params, HTTPRequestAction.Login).doRequest(this,
                        (response) -> {
                            responseCode = HTTPRequest.extractResponseCode(response);

                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("username_login", username);
                            editor.putString("password_login", password);
                            editor.putString("userdata_login", HTTPRequest.extractResponseBody(response));
                            editor.apply();

                            storeLastAccess();
                            onFinishLogin(response);
                        },
                        (error) -> {
                            responseCode = 404;
                            onErrorLogin();
                        });
            }
        }
    }

    public void onFinishLogin(String response) {
        //Hide the progress bar
        findViewById(R.id.progressLogin).setVisibility(View.INVISIBLE);

        if (responseCode == 200) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("workflow_user", response);
            editor.apply();

            Intent intent = new Intent(LoginActivity.this, WorkflowActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(LoginActivity.this, StatusActivity.class);
            intent.putExtra("REQUEST_STATUS", responseCode);
            intent.putExtra("ACTION_TYPE", "LOGIN_TYPE");

            startActivity(intent);
        }
    }

    public void onErrorLogin() {
        //Hide the progress bar
        findViewById(R.id.progressLogin).setVisibility(View.INVISIBLE);

        Intent intent = new Intent(LoginActivity.this, StatusActivity.class);
        intent.putExtra("REQUEST_STATUS", responseCode);

        startActivity(intent);
    }

    public void storeLastAccess() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("last_access_login", sdf.format(new Date()));

        editor.apply();
    }

    public void loadLastAccess() {
        String textLastAccess = getString(R.string.last_access);
        String textNoAccess = getString(R.string.no_access);

        TextView lastAccess = findViewById(R.id.lastAccessLogin);
        lastAccess.setText(textLastAccess + " " + prefs.getString("last_access_login", textNoAccess));
    }

}

