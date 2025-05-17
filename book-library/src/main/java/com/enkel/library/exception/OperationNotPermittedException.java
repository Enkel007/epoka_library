package com.enkel.library.exception;

public class OperationNotPermittedException extends RuntimeException {
    public OperationNotPermittedException(String message) {
        super(message);
    }
}
