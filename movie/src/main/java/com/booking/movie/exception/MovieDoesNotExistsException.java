package com.booking.movie.exception;

public class MovieDoesNotExistsException extends RuntimeException{

    public MovieDoesNotExistsException(String message){
        super(message);
    }

}
