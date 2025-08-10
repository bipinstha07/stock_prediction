package com.stock.predictioin.exceptionHandler;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(){
        super("Resource Not Found");
    }
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
