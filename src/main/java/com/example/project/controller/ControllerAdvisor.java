package com.example.project.controller;

import com.example.project.exception.ConditionsNotMetException;
import com.example.project.exception.DataNotFound;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class ControllerAdvisor extends ResponseEntityExceptionHandler {
    @ExceptionHandler({DataNotFound.class,
            IllegalArgumentException.class,
            ConditionsNotMetException.class})
    public ResponseEntity<Object> handleDataNotFoundException(Exception ex, WebRequest webRequest) {
        logger.warn(ex.getMessage());
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST, webRequest);

    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> validationErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().stream()
                .filter(error -> error instanceof FieldError)
                .map(objectError -> (FieldError) objectError)
                .forEach(objectError -> {
                    String fieldName = objectError.getField();
                    String errorMessage = objectError.getDefaultMessage();

                    logger.warn(errorMessage);
                    validationErrors.put(fieldName, errorMessage);
                });
        return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleValidationException(Exception ex, WebRequest webRequest) {
        int i = ex.getMessage().indexOf(" ");
        String errorMessage = ex.getMessage().substring(i);
        log.warn(errorMessage);
        return handleExceptionInternal(ex, errorMessage, new HttpHeaders(), HttpStatus.BAD_REQUEST, webRequest);
    }

}
