package com.booking.show.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name="theatres")
@Getter
@Setter
public class TheatreEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    private String theatreId;

    private String theatreName;

    private String location;

    private String theatreType;

}
