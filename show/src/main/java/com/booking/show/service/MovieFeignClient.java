package com.booking.show.service;

import com.booking.show.dto.MoviesDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("movies")
public interface MovieFeignClient {

    @GetMapping("/api/findMovie")
    public ResponseEntity<List<MoviesDto>> findMovies(@RequestParam(defaultValue = "") String movieId,
                                                      @RequestParam(defaultValue = "") String genreType,
                                                      @RequestParam(defaultValue = "") String language);

}
