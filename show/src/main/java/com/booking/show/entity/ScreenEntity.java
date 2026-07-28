package com.booking.show.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name="screens")
@Getter
@Setter
public class ScreenEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    private String screenId;

    private String theatreId;

    private String screenName;

    private String screenStatus;

}
