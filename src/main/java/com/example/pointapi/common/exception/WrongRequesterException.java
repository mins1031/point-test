package com.example.pointapi.common.exception;

public class WrongRequesterException extends RuntimeException {

    public WrongRequesterException(String message) {
        super(message);
    }
}
