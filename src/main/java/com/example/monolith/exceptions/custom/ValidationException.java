package com.example.monolith.exceptions.custom;

import java.util.Map;

import lombok.Getter;

public class ValidationException extends RuntimeException{
    
    @Getter
    private final Map<String, String> errors;

    public ValidationException(String message, Map<String, String> errors) {
        super(message);
        this.errors = errors;
    }
}
