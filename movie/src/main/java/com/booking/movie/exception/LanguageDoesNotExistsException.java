package com.booking.movie.exception;

public class LanguageDoesNotExistsException extends RuntimeException{

    public LanguageDoesNotExistsException(String message){
        super(message);
    }

}
