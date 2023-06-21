package dev.country.api.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String errorMessage) {
        super(errorMessage);
    }

    public NotFoundException() {
    }
}



