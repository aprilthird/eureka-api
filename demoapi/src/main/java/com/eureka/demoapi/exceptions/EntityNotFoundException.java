package com.eureka.demoapi.exceptions;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String exception) {
        super(exception);
    }
}
