package com.petbackend.thbao.exceptions;

public class InvalidDateException extends RuntimeException{
    public InvalidDateException(String message) {
        super(message);
    }
}
