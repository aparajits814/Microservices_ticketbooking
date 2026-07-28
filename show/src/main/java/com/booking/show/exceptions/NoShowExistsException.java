package com.booking.show.exceptions;

public class NoShowExistsException extends RuntimeException{

    public NoShowExistsException(String message){
        super(message);
    }

}
