package com.example.taskprueba.common.web;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.taskprueba.common.exceptions.SystemException;

@RestControllerAdvice
public class ValidationHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handle(
            MethodArgumentNotValidException ex
    ) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(e ->
            errors.put(e.getField(), e.getDefaultMessage())
        );

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(SystemException.class)
    public ResponseEntity<Map<String, Object>> handle(SystemException ex) {
        Map<String, Object> errors = new HashMap<>();
        errors.put("errorName", ex.getClass().getSimpleName());
        errors.put("message", ex.getMessage());
        return ResponseEntity.status(400).body(errors);
    }
}
