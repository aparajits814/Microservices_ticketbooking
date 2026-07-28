package com.booking.ticketBooking.service;


import com.booking.ticketBooking.constants.BookingConstants;
import com.booking.ticketBooking.dto.*;
import com.booking.ticketBooking.entity.BookingEntity;
import com.booking.ticketBooking.entity.BookingSeatEntity;
import com.booking.ticketBooking.exception.BookingNotExistsException;
import com.booking.ticketBooking.exception.IllegalBookingException;
import com.booking.ticketBooking.exception.ResourceNotFoundException;
import com.booking.ticketBooking.exception.SeatUnavailableException;
import com.booking.ticketBooking.mapper.BookingMapper;
import com.booking.ticketBooking.repository.BookingRepository;
import com.booking.ticketBooking.repository.BookingSeatRepository;
import feign.FeignException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService{

    private BookingRepository bookingRepository;

    private BookingSeatRepository bookingSeatRepository;

    private ShowFeignClient showFeignClient;

    @Override
    @Transactional
    public BookingDto createBooking(BookingDto bookingDto) {

        String bookingId = UUID.randomUUID().toString();
        System.out.println("Booking ID:"+bookingId);

        SeatsLockDto seatsLockDto = new SeatsLockDto();

        seatsLockDto.setBookingId(bookingId);
        seatsLockDto.setSeatIds(bookingDto.getSeatIdList()
                .stream().map(SeatDto::getSeatId).toList());
        seatsLockDto.setShowId(bookingDto.getShowId());

        SeatLockResponseDto seatLockResponseDto;

        try {

            seatLockResponseDto = showFeignClient.lockSeats(seatsLockDto).getBody();

        }catch(FeignException.Conflict exception){

            throw new SeatUnavailableException(BookingConstants.SEATS_UNAVAILABLE_EXCEPTION);

        }catch(FeignException.BadRequest exception){

            throw new IllegalBookingException(BookingConstants.ILLEGAL_BOOKING_EXCEPTION);

        }catch(FeignException.ServiceUnavailable exception){

            throw new ResourceNotFoundException(BookingConstants.SERVICE_DOWN);

        }


        ShowInfoDto showInfoDto = seatLockResponseDto.getShowInfo();

        BigDecimal totalPrice = findTotalPrice(showInfoDto,bookingDto);
        System.out.println("Total Price:"+totalPrice);

        BookingEntity bookingEntity = BookingMapper.mapBookingDtoToBookingEntity(bookingDto);

        bookingEntity.setPaymentAmount(totalPrice);
        bookingEntity.setBookingId(bookingId);
        System.out.println("Entity:"+bookingEntity);

        bookingRepository.save(bookingEntity);

        bookingDto.setBookingId(bookingId);

        List<BookingSeatEntity> bookingSeatEntityList = BookingMapper.mapListBookingIdToBookingSeatEntity(bookingDto);

        bookingSeatRepository.saveAll(bookingSeatEntityList);

        bookingDto.setBookingStatus(BookingConstants.BOOKING_STATUS_PENDING);

        return bookingDto;
    }

    @Override
    public BookingInfoDto findBooking(String bookingId) {

        Optional<BookingEntity> bookingEntityOptional = bookingRepository.findById(bookingId);

        if(bookingEntityOptional.isEmpty()){
            throw new BookingNotExistsException(BookingConstants.BOOKING_NOT_FOUND);
        }

        BookingInfoDto bookingInfoDto = new BookingInfoDto();

        bookingInfoDto.setBookingId(bookingId);
        bookingInfoDto.setBookingStatus(bookingEntityOptional.get().getBookingStatus());
        bookingInfoDto.setPrice(bookingEntityOptional.get().getPaymentAmount());
        bookingInfoDto.setShowId(bookingEntityOptional.get().getShowId());

        return bookingInfoDto;
    }

    private BigDecimal findTotalPrice(ShowInfoDto showInfoDto, BookingDto bookingDto){

        List<SeatPricingDto> seatsPricingDtoList = null;
        if (showInfoDto != null) {
            seatsPricingDtoList = showInfoDto.getSeatsPricing();
        }

        Map<String,BigDecimal> seatPriceMap = new HashMap<>();

        for(SeatPricingDto seatPricingDto : seatsPricingDtoList){
            seatPriceMap.put(seatPricingDto.getSeatType(),seatPricingDto.getSeatPrice());
        }

        BigDecimal totalPrice = BigDecimal.ZERO;

        for(SeatDto seatDto : bookingDto.getSeatIdList()){

            BigDecimal seatPrice = seatPriceMap.get(seatDto.getSeatType());

            totalPrice = totalPrice.add(seatPrice);

        }

        return totalPrice;

    }
}
