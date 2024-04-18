package com.petbackend.thbao.exceptions;

public class AlreadyExistingException extends RuntimeException{
    public AlreadyExistingException (String message){
        super(message);
    }
}
