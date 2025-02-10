package br.com.edielsonassis.course.services.exceptions;

public class ValidationException extends RuntimeException {
    
    public ValidationException(String msg) {
        super(msg);
    }
}