package com.booking.movie.exception;

public class GenreDoesNotExistsException extends RuntimeException{

    public GenreDoesNotExistsException(String message){
        super(message);
    }

}
