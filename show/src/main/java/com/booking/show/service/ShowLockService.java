package com.booking.show.service;

import com.booking.show.dto.SeatLockResponseDto;
import com.booking.show.dto.SeatsLockDto;

public interface ShowLockService {

    SeatLockResponseDto lockSeats(SeatsLockDto seatsLockDto);

}
