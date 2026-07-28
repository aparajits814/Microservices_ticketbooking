package com.booking.movie.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "languages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LanguagesEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    private String languageId;

    private String language;

    @ManyToMany(mappedBy = "languages")
    private Set<MoviesEntity> movies = new HashSet<>();
}
