package com.booking.show.controller;

import com.booking.show.dto.ShowDto;
import com.booking.show.dto.ShowInfoDto;
import com.booking.show.service.ShowService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(value="/api", produces = {MediaType.APPLICATION_JSON_VALUE})
public class ShowController {

    private ShowService showService;

    @PostMapping("/createShow")
    public ResponseEntity<ShowDto> createShow(@RequestBody ShowDto showDto){

        ShowDto showDtoCreated = showService.createShow(showDto);

        return new ResponseEntity<>(showDtoCreated, HttpStatus.CREATED);

    }

    @GetMapping("/findMovie")
    public ResponseEntity<List<ShowDto>> findAllShows(
            @RequestParam String movieId,
            @RequestParam String location
    ){
        List<ShowDto> showDtoList = showService.findShowByMovie(movieId, location).getShowList();

        return new ResponseEntity<>(showDtoList,HttpStatus.OK);

    }

    @GetMapping("/findShow")
    public ResponseEntity<ShowInfoDto> findShow(@RequestParam String showId){

        ShowInfoDto showInfoDto = showService.findShowByShowId(showId);

        return new ResponseEntity<>(showInfoDto,HttpStatus.OK);

    }


}
