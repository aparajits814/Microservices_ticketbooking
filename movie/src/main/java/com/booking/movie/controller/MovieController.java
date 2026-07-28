package com.booking.movie.controller;

import com.booking.movie.dto.MoviesDto;
import com.booking.movie.dto.ResponseDto;
import com.booking.movie.service.MovieService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
public class MovieController {

    private MovieService movieService;

    @PostMapping("/createMovie")
    public ResponseEntity<ResponseDto> createMovie(@RequestBody MoviesDto moviesDto){

        ResponseDto responseDto = movieService.createMovie(moviesDto);

        return new ResponseEntity<ResponseDto>(responseDto, HttpStatus.CREATED);

    }

    @GetMapping("/findMovie")
    public ResponseEntity<List<MoviesDto>> findMovies(@RequestParam(defaultValue = "") String movieId,
                                                      @RequestParam(defaultValue = "") String genreType,
                                                      @RequestParam(defaultValue = "") String language){

        List<MoviesDto> moviesList = movieService.findMovie(movieId,genreType,language).getMoviesList();
        return new ResponseEntity<List<MoviesDto>>(moviesList,HttpStatus.OK);
    }

    @DeleteMapping("/deleteMovie")
    public ResponseEntity<ResponseDto> deleteMovie(@RequestParam String movieId){

        ResponseDto responseDto = movieService.deleteMovie(movieId);

        return new ResponseEntity<>(responseDto,HttpStatus.OK);

    }


}
