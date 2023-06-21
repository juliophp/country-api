package dev.country.api.exception;

public class AuthenticationException extends RuntimeException {

    public AuthenticationException(String errorMessage) {
        super(errorMessage);
    }

    public AuthenticationException() {
    }
}
