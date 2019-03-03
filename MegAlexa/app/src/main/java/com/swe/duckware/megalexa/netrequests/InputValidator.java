package com.swe.duckware.megalexa.netrequests;

import android.util.Patterns;

public class InputValidator {

    public static final int MIN_PASSWORD_COUNT = 10;

    private String username;

    private String password;

    public InputValidator(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public boolean isEmailValid() {
        return Patterns.EMAIL_ADDRESS.matcher(username).matches();
    }

    public boolean isPasswordValid() {
        return password.length() >= MIN_PASSWORD_COUNT;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
