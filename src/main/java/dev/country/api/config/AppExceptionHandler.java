package dev.country.api.config;


import dev.country.api.exception.BadRequestException;
import dev.country.api.dto.BaseResponse;
import dev.country.api.exception.NotFoundException;
import dev.country.api.exception.ThirdPartyAPIException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class AppExceptionHandler {


    @ResponseBody
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handleException(BadRequestException exception) {
        BaseResponse response = new BaseResponse("95", exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ResponseBody
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(NotFoundException exception) {
        BaseResponse response = new BaseResponse("93",
                exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @ResponseBody
    @ExceptionHandler(ThirdPartyAPIException.class)
    public ResponseEntity<?> handleThirdPartyAPIException(ThirdPartyAPIException exception) {
        BaseResponse response = new BaseResponse("90",
                exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception exception) {
        BaseResponse response = new BaseResponse("98",
                "Something went wrong, please contact admin");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }



}

