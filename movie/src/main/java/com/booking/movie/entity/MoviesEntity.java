package com.booking.movie.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name="movies")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MoviesEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    private String movieId;

    private String movieName;

    private BigDecimal reviews;

    private Integer duration;

    private LocalDate releaseDate;

    @ManyToMany
    @JoinTable(
            name = "movies_genre",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<GenreEntity> genres;

    @ManyToMany
    @JoinTable(
            name = "movies_language",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "language_id")
    )
    private Set<LanguagesEntity> languages;

}
