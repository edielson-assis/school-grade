package br.com.edielsonassis.course.services.exceptions;

public class ObjectNotFoundException extends RuntimeException {
    
    public ObjectNotFoundException(String msg) {
        super(msg);
    }
}