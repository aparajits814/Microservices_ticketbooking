package com.booking.show.exceptions;

import com.booking.show.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MovieNotExistsException.class)
    public ResponseEntity<ErrorResponseDto> handleMovieNotExistsException(MovieNotExistsException exception){
        ErrorResponseDto errorResponseDTO = new ErrorResponseDto(exception.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoShowExistsException.class)
    public ResponseEntity<ErrorResponseDto> handleNoShowExistsException(NoShowExistsException exception){
        ErrorResponseDto errorResponseDTO = new ErrorResponseDto(exception.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ShowAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDto> handleShowAlreadyExistsException(ShowAlreadyExistsException exception){
        ErrorResponseDto errorResponseDTO = new ErrorResponseDto(exception.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalShowException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalShowException(IllegalShowException exception){
        ErrorResponseDto errorResponseDTO = new ErrorResponseDto(exception.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ShowInactiveException.class)
    public ResponseEntity<ErrorResponseDto> handleShowInactiveException(ShowInactiveException exception){
        ErrorResponseDto errorResponseDTO = new ErrorResponseDto(exception.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SeatUnavailableException.class)
    public ResponseEntity<ErrorResponseDto> handleSeatUnavailableException(SeatUnavailableException exception){
        ErrorResponseDto errorResponseDTO = new ErrorResponseDto(exception.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.CONFLICT);
    }

}
