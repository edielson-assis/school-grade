package br.com.edielsonassis.authuser.services.exceptions;

public class ValidationException extends RuntimeException {
    
    public ValidationException(String message) {
        super(message);
    }
}