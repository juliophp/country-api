package dev.country.api.exception;

public class ThirdPartyAPIException extends RuntimeException {

    public ThirdPartyAPIException(String errorMessage) {
        super(errorMessage);
    }

    public ThirdPartyAPIException() {
    }
}
