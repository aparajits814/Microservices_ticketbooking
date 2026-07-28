package com.booking.show.exceptions;

public class MovieNotExistsException extends RuntimeException{

    public MovieNotExistsException(String movieId){
        super("Movie With ID:"+movieId+" does not Exists");
    }

}
