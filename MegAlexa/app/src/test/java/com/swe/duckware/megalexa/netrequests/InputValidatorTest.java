package com.swe.duckware.megalexa.netrequests;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class InputValidatorTest {

    private InputValidator in;

    @Before
    public void setUp() {
        in = new InputValidator("prova@test.com", "1234567890");
    }

    @Test
    public void password_valid() {
        assertTrue(in.isPasswordValid());
    }

    @Test
    public void password_length() {
        assertTrue(in.getPassword().length() >= 10);
    }

}
