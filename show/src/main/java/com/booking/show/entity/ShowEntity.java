package com.booking.show.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Entity
@Table(name="shows")
@Getter
@Setter
public class ShowEntity {


    @Id
    @GeneratedValue
    @UuidGenerator
    private String showId;

    private String movieId;

    private String screenId;

    private LocalDateTime showStartTime;

    private LocalDateTime showEndTime;

    private String showStatus;

}
