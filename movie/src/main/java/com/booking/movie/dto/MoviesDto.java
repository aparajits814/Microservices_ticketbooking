package com.booking.movie.dto;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MoviesDto implements Serializable {

    private String movieName;

    private List<String> genres;

    private Integer duration;

    private BigDecimal rating;

    private List<String> languages;

}
