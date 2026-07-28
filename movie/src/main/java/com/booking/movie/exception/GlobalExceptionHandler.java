package com.booking.movie.exception;

import com.booking.movie.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MovieAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDto> handleLoanAlreadyExistsException(MovieAlreadyExistsException exception){
        ErrorResponseDto errorResponseDTO = new ErrorResponseDto(exception.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(GenreDoesNotExistsException.class)
    public ResponseEntity<ErrorResponseDto> handleGenreDoesNotExistsException(GenreDoesNotExistsException exception){
        ErrorResponseDto errorResponseDTO = new ErrorResponseDto(exception.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(LanguageDoesNotExistsException.class)
    public ResponseEntity<ErrorResponseDto> handleLanguageDoesNotExistsException(LanguageDoesNotExistsException exception){
        ErrorResponseDto errorResponseDTO = new ErrorResponseDto(exception.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MovieDoesNotExistsException.class)
    public ResponseEntity<ErrorResponseDto> handleMovieDoesNotExistsException(MovieDoesNotExistsException exception){
        ErrorResponseDto errorResponseDTO = new ErrorResponseDto(exception.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.NOT_FOUND);
    }

}
