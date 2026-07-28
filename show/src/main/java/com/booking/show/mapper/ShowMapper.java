package com.booking.show.mapper;

import com.booking.show.constants.ShowConstants;
import com.booking.show.dto.SeatPricingDto;
import com.booking.show.dto.ShowDto;
import com.booking.show.entity.ShowEntity;
import com.booking.show.entity.ShowPricingEntity;

import java.util.ArrayList;
import java.util.List;

public class ShowMapper {

    private ShowMapper(){

    }

    public static ShowEntity mapShowDtoToShowEntity(ShowDto showDto){
        ShowEntity showEntity = new ShowEntity();
        showEntity.setMovieId(showDto.getMovieId());
        showEntity.setScreenId(showDto.getScreenId());
        showEntity.setShowStatus(ShowConstants.SHOW_STATUS_ACTIVE);
        showEntity.setShowStartTime(showDto.getStartTime());
        showEntity.setShowEndTime(showDto.getEndTime());
        return showEntity;
    }

    public static List<ShowPricingEntity> maplistShowPricingDtoToListShowPricingEntity(List<SeatPricingDto> seatPriceList,String showId){
        List<ShowPricingEntity> showPricingList = new ArrayList<>();

        for(SeatPricingDto seatPricingDto: seatPriceList){
            ShowPricingEntity showPricingEntity = new ShowPricingEntity();
            showPricingEntity.setPrice(seatPricingDto.getSeatPrice());
            showPricingEntity.setShowId(showId);
            showPricingEntity.setSeatType(seatPricingDto.getSeatType());
            showPricingList.add(showPricingEntity);
        }
        return showPricingList;
    }

    public static List<ShowDto> mapListShowEntityToListShowDto(List<ShowEntity> showEntityList){
        List<ShowDto> showDtoList = new ArrayList<>();

        for(ShowEntity showEntity: showEntityList){
            ShowDto showDto = new ShowDto();
            showDto.setShowId(showEntity.getShowId());
            showDto.setScreenId(showEntity.getScreenId());
            showDto.setMovieId(showEntity.getMovieId());
            showDto.setStartTime(showEntity.getShowStartTime());
            showDto.setEndTime(showEntity.getShowEndTime());
            showDtoList.add(showDto);
        }

        return showDtoList;
    }

    public static List<SeatPricingDto> mapListShowPricingEntityToListSeatPricingDto(List<ShowPricingEntity> showPricingEntityList){
        List<SeatPricingDto> seatPricingDtoList = new ArrayList<>();

        for(ShowPricingEntity showPricingEntity : showPricingEntityList) {

            SeatPricingDto seatPricingDto = new SeatPricingDto();
            seatPricingDto.setSeatPrice(showPricingEntity.getPrice());
            seatPricingDto.setSeatType(showPricingEntity.getSeatType());

            seatPricingDtoList.add(seatPricingDto);

        }

        return seatPricingDtoList;
    }

}
