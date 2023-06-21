package dev.country.api.exception;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.ObjectError;

import java.util.List;
import java.util.stream.Collectors;

public class BadRequestException extends RuntimeException {

    public BadRequestException(List<ObjectError> errors) {
        super(errors.stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(",")));
    }
    public BadRequestException(String error) {
        super(error);
    }

    public BadRequestException() {
    }
}