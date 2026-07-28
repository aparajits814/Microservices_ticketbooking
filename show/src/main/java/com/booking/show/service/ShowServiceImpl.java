package com.booking.show.service;

import com.booking.show.constants.ShowConstants;
import com.booking.show.dto.*;
import com.booking.show.entity.*;
import com.booking.show.exceptions.MovieNotExistsException;
import com.booking.show.exceptions.NoShowExistsException;
import com.booking.show.exceptions.ShowAlreadyExistsException;
import com.booking.show.mapper.ShowMapper;
import com.booking.show.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ShowServiceImpl implements ShowService{

    private MovieFeignClient movieFeignClient;

    private ShowPricingRepository showPricingRepository;

    private ShowRepository showRepository;

    private TheatreRepository theatreRepository;

    private ScreenRepository screenRepository;

    private SeatRepository seatRepository;

    private ShowSeatRepository showSeatRepository;

    @Override
    @Transactional
    public ShowDto createShow(ShowDto showDto) {

        List<MoviesDto> movie = movieFeignClient.findMovies(showDto.getMovieId(),"","").getBody();

        if(movie == null || movie.isEmpty()){
            throw new MovieNotExistsException(showDto.getMovieId());
        }

        boolean showExists = showRepository.existsByScreenIdAndShowStartTimeLessThanAndShowEndTimeGreaterThan(showDto.getScreenId(),showDto.getEndTime(),showDto.getStartTime());

        System.out.println(showExists);

        if(showExists){
            throw new ShowAlreadyExistsException(ShowConstants.SHOW_EXISTS_MESSAGE);
        }

        List<SeatEntity> seatEntityList = seatRepository.findByScreenId(showDto.getScreenId());

        ShowEntity showEntity = ShowMapper.mapShowDtoToShowEntity(showDto);

        ShowEntity showEntitySaved = showRepository.save(showEntity);

        List<ShowPricingEntity> showPricingEntityList = ShowMapper.maplistShowPricingDtoToListShowPricingEntity(showDto.getSeatPricing(), showEntity.getShowId());

        showPricingRepository.saveAll(showPricingEntityList);

        List<ShowSeatEntity> showSeatEntityList = new ArrayList<>();

        for(SeatEntity seatEntity : seatEntityList){

            ShowSeatEntity showSeatEntity = new ShowSeatEntity();
            showSeatEntity.setSeatStatus(ShowConstants.SEAT_STATUS_AVALIABLE);
            showSeatEntity.setShowId(showEntity.getShowId());
            showSeatEntity.setSeatId(seatEntity.getSeatId());
            showSeatEntityList.add(showSeatEntity);

        }

        showSeatRepository.saveAll(showSeatEntityList);

        showDto.setShowId(showEntitySaved.getShowId());

        return showDto;

    }

    @Override
    public ResponseDto findShowByMovie(String movieId, String location) {

        ResponseDto responseDto = new ResponseDto();

        List<ShowEntity> showList = showRepository.findByMovieId(movieId);

        if(showList == null || showList.isEmpty()){
            throw new NoShowExistsException(ShowConstants.SHOW_DOES_NOT_EXISTS_MESSAGE);
        }

        List<TheatreEntity> theatreEntityList = theatreRepository.findByLocation(location);

        List<ScreenEntity> screenEntityList = new ArrayList<>();

        for(TheatreEntity theatreEntity : theatreEntityList){

            List<ScreenEntity> screenEntityListByTheatreId = screenRepository.findByTheatreId(theatreEntity.getTheatreId());

            screenEntityList.addAll(screenEntityListByTheatreId);

        }

        Set<String> screenIdSet = screenEntityList.stream()
                .map(ScreenEntity::getScreenId)
                .collect(Collectors.toSet());

        List<ShowEntity> filteredShowsList = showList.stream()
                .filter(showEntity -> screenIdSet.contains(showEntity.getScreenId()))
                .toList();

        List<ShowDto> showDtoList = ShowMapper.mapListShowEntityToListShowDto(filteredShowsList);

        for(ShowDto showDto : showDtoList) {

            List<ShowPricingEntity> showPricingEntityList = showPricingRepository.findByShowId(showDto.getShowId());

            List<SeatPricingDto> seatPricingDtoList = ShowMapper.mapListShowPricingEntityToListSeatPricingDto(showPricingEntityList);

            showDto.setSeatPricing(seatPricingDtoList);

        }

        responseDto.setShowList(showDtoList);

        return responseDto;
    }

    @Override
    public ShowInfoDto findShowByShowId(String showId) {

        ShowInfoDto showInfoDto = new ShowInfoDto();

        showInfoDto.setShowId(showId);

        Optional<ShowEntity> showEntityOptional = showRepository.findById(showId);

        if(showEntityOptional.isEmpty()){
            throw new NoShowExistsException(ShowConstants.SHOW_DOES_NOT_EXISTS_MESSAGE);
        }

        showInfoDto.setMovieId(showEntityOptional.get().getMovieId());
        showInfoDto.setShowStartTime(showEntityOptional.get().getShowStartTime());
        showInfoDto.setShowEndTime(showEntityOptional.get().getShowEndTime());
        showInfoDto.setSeatIds(new ArrayList<>());
        showInfoDto.setShowStatus(showEntityOptional.get().getShowStatus());


        Optional<ScreenEntity> screenEntityOptional = screenRepository.findById(showEntityOptional.get().getScreenId());

        if(screenEntityOptional.isEmpty()){
            return showInfoDto;
        }

        List<SeatEntity> seatEntityList = seatRepository.findByScreenId(screenEntityOptional.get().getScreenId());

        List<String> seatIdList = seatEntityList.stream()
                .map(SeatEntity::getSeatId)
                .toList();

        showInfoDto.setSeatIds(seatIdList);

        List<ShowPricingEntity> showPriceList = showPricingRepository.findByShowId(showId);

        List<SeatPricingDto> seatPricingDtoList = ShowMapper.mapListShowPricingEntityToListSeatPricingDto(showPriceList);

        showInfoDto.setSeatsPricing(seatPricingDtoList);

        return showInfoDto;
    }
}
