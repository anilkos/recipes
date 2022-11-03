package com.abnamro.recipes.exception;

public class LoginSessionExpiredException extends RuntimeException {
    public LoginSessionExpiredException(String message) { super(message);
    }
}
