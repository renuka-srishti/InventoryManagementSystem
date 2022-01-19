package com.shopify.ims.controller;

import com.shopify.ims.exception.GroupNotExistException;
import com.shopify.ims.exception.InventoryNotExistException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Log4j2
@RestControllerAdvice
public class ExceptionHandlingController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(GroupNotExistException.class)
    public ResponseEntity handleGroupNotExistException(GroupNotExistException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(InventoryNotExistException.class)
    public ResponseEntity handleInventoryNotExistException(InventoryNotExistException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.fatal(String.format("Request failed with exception: %s", ex.getMessage()), ex);
        return super.handleExceptionInternal(ex, body, headers, status, request);
    }
}
