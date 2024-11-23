package org.example.taskmanager.controller.advice;

import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.example.taskmanager.exception.EntityNotFoundException;
import org.example.taskmanager.exception.ErrorResponse;
import org.example.taskmanager.exception.ExceptionMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@RequiredArgsConstructor
public class ControllerAdvice {

    @ExceptionHandler({HttpMessageNotReadableException.class, ConstraintViolationException.class})
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(Exception e, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(ExceptionMessage.BINDING_RESULTS_ERROR.getMessage(), HttpStatus.BAD_REQUEST, request);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleNotFoundException(Exception e, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND, request);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponse> handleMethodException(MethodArgumentNotValidException e, WebRequest request) {
        List<FieldError> errors = e.getFieldErrors();
        String message = errors.stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .filter(StringUtils::isNotEmpty)
                .collect(Collectors.joining(", "));
        ErrorResponse errorResponse = new ErrorResponse(message, HttpStatus.BAD_REQUEST, request);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
