package com.booking.movie.service;

import com.booking.movie.dto.MoviesDto;
import com.booking.movie.dto.MoviesList;
import com.booking.movie.dto.ResponseDto;
import org.apache.coyote.Response;

import java.util.List;

public interface MovieService {

    ResponseDto createMovie(MoviesDto moviesDto);

    ResponseDto deleteMovie(String movieName);

    MoviesList findMovie(String movieId, String genreType, String language);

}
