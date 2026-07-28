package com.booking.movie.service;

import com.booking.movie.constants.MovieConstants;
import com.booking.movie.dto.MoviesDto;
import com.booking.movie.dto.MoviesList;
import com.booking.movie.dto.ResponseDto;
import com.booking.movie.entity.GenreEntity;
import com.booking.movie.entity.LanguagesEntity;
import com.booking.movie.entity.MoviesEntity;
import com.booking.movie.exception.GenreDoesNotExistsException;
import com.booking.movie.exception.LanguageDoesNotExistsException;
import com.booking.movie.exception.MovieAlreadyExistsException;
import com.booking.movie.exception.MovieDoesNotExistsException;
import com.booking.movie.mapper.MovieMapper;
import com.booking.movie.repository.GenreRepository;
import com.booking.movie.repository.LanguagesRepository;
import com.booking.movie.repository.MoviesRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@AllArgsConstructor
public class MovieServiceImpl implements MovieService {

    private MoviesRepository moviesRepository;

    private GenreRepository genreRepository;

    private LanguagesRepository languagesRepository;

    @Override
    @Transactional
    public ResponseDto createMovie(MoviesDto moviesDto) {

        Optional<MoviesEntity> movieOptional = moviesRepository.findByMovieName(moviesDto.getMovieName());

        if(movieOptional.isPresent()){
            throw new MovieAlreadyExistsException(MovieConstants.MOVIE_ALREADY_EXISTS);
        }

        Set<GenreEntity> genresSet = new HashSet<>();

        for(String genre : moviesDto.getGenres()){

            Optional<GenreEntity> genreEntityOptional = genreRepository.findByGenreType(genre);

            if(genreEntityOptional.isEmpty()){
                throw new GenreDoesNotExistsException("Genre: "+genre+" does not exists");
            }

            genresSet.add(genreEntityOptional.get());

        }

        Set<LanguagesEntity> languagesSet = new HashSet<>();

        for(String language : moviesDto.getLanguages()){

            Optional<LanguagesEntity> languagesEntityOptional = languagesRepository.findByLanguage(language);

            if(languagesEntityOptional.isEmpty()){
                throw new LanguageDoesNotExistsException("Language: "+language+" is invalid");
            }

            languagesSet.add(languagesEntityOptional.get());
        }

        MoviesEntity moviesEntity = MovieMapper.mapToMovieEntityFromMoviesDto(moviesDto);

        moviesEntity.setGenres(genresSet);
        moviesEntity.setLanguages(languagesSet);

        moviesRepository.save(moviesEntity);

        return new ResponseDto(HttpStatus.OK,MovieConstants.MOVIE_SUCCESSFULLY_ADDED);
    }

    @Override
    @Transactional
    @CacheEvict(value = "movies", key = "#movieId")
    public ResponseDto deleteMovie(String movieId) {

        Optional<MoviesEntity> moviesEntityOptional = moviesRepository.findById(movieId);

        if(moviesEntityOptional.isEmpty()){
            throw new MovieDoesNotExistsException(MovieConstants.MOVIE_DOES_NOT_EXISTS);
        }

        moviesRepository.delete(moviesEntityOptional.get());

        return new ResponseDto(HttpStatus.OK, MovieConstants.MOVIE_SUCCESSFULLY_DELETE);
    }

    @Override
    @Cacheable(value = "movies", key = "#movieId")
    public MoviesList findMovie(String movieId, String genreType, String language) {

        System.out.println("Inside method");

        MoviesList list = new MoviesList();

        List<MoviesEntity> movies;

        System.out.println(movieId);

        if (!movieId.isBlank()) {
            Optional<MoviesEntity> movie = moviesRepository.findById(movieId);
            if(movie.isEmpty()){
                return list;
            }
            movies = List.of(movie.get());
        } else {
            movies = moviesRepository.findAll();
        }

        list.setMoviesList(movies.stream()
                .filter(movie -> genreType.isBlank() ||
                        movie.getGenres().stream()
                                .anyMatch(g -> g.getGenreType().equalsIgnoreCase(genreType)))
                .filter(movie -> language.isBlank() ||
                        movie.getLanguages().stream()
                                .anyMatch(l -> l.getLanguage().equalsIgnoreCase(language)))
                .map(MovieMapper::mapToMoviesDtoFromMoviesEntity)
                .toList());

        return list;
    }
}
