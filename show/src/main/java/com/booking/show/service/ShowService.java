package com.booking.show.service;

import com.booking.show.dto.ResponseDto;
import com.booking.show.dto.ShowDto;
import com.booking.show.dto.ShowInfoDto;

public interface ShowService {

    ShowDto createShow(ShowDto showDto);

    ResponseDto findShowByMovie(String movieId, String location);

    ShowInfoDto findShowByShowId(String showId);

}
