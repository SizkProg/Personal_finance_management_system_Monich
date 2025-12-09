package com.example.springexample.Exceptions;

public class PasswordIsNotCorrect extends RuntimeException {
    public PasswordIsNotCorrect(String message) {
        super(message);
    }
}
