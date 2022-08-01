package com.backbase.openbanking.mockserver.common.web;

import com.backbase.openbanking.mockserver.common.exceptions.MissingFieldException;
import com.backbase.openbanking.mockserver.common.exceptions.MockDataException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * This class is responsible to manage all the exceptions that are thrown by a controller and it's not caught with a catch
 * block, for example the {@link MissingFieldException} is throw by the {@link AbstractMockController} to finish the execution
 * of a method when a field is missing, this exception is managed in this class {@link ResponseExceptionHandler#handleMissingFieldException(MissingFieldException, WebRequest)}
 * and it creates the correspond Response.
 * 
 * @author cesarl
 */
@ControllerAdvice
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = MissingFieldException.class)
    public  ResponseEntity<Object> handleMissingFieldException(MissingFieldException ex, WebRequest request) {
        String bodyOfResponse = "The field " + ex.getFieldName() + " is missing in the request";
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.BAD_GATEWAY, request);
    }

    @ExceptionHandler(value = MockDataException.class)
    public ResponseEntity<Object> handleMockDataException(MockDataException ex, WebRequest request) {
        String bodyOfResponse = "An unexpected error occur processing the mock data " + ex.getMessage();
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.BAD_GATEWAY, request);
    }
}
