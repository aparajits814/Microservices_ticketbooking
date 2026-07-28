package com.booking.movie.mapper;

import com.booking.movie.dto.MoviesDto;
import com.booking.movie.entity.GenreEntity;
import com.booking.movie.entity.LanguagesEntity;
import com.booking.movie.entity.MoviesEntity;

import java.math.BigDecimal;
import java.util.stream.Collectors;

public class MovieMapper {

    public MovieMapper(){

    }

    public static MoviesEntity mapToMovieEntityFromMoviesDto(MoviesDto moviesDto){
        MoviesEntity moviesEntity = new MoviesEntity();
        moviesEntity.setMovieName(moviesDto.getMovieName());
        moviesEntity.setReviews(moviesDto.getRating());
        moviesEntity.setDuration(moviesDto.getDuration());
        return moviesEntity;
    }

    public static MoviesDto mapToMoviesDtoFromMoviesEntity(MoviesEntity moviesEntity){
        MoviesDto moviesDto = new MoviesDto();
        moviesDto.setRating(BigDecimal.valueOf(moviesEntity.getDuration()));
        moviesDto.setMovieName(moviesEntity.getMovieName());
        moviesDto.setLanguages(moviesEntity
                .getLanguages().stream()
                .map(LanguagesEntity::getLanguage).collect(Collectors.toList()));
        moviesDto.setGenres(moviesEntity
                .getGenres().stream()
                .map(GenreEntity::getGenreType).collect(Collectors.toList()));

        return moviesDto;
    }


}
