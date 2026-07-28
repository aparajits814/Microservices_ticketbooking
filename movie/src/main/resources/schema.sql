CREATE TABLE IF NOT EXISTS movies (
    movie_id VARCHAR(100) PRIMARY KEY,
    movie_name VARCHAR(100) NOT NULL,
    reviews INT NOT NULL,
    duration INT NOT NULL,
    release_date DATE
    );

CREATE TABLE IF NOT EXISTS genres (
    genre_id VARCHAR(100) PRIMARY KEY,
    genre_type VARCHAR(100) NOT NULL
    );

CREATE TABLE IF NOT EXISTS languages (
    language_id VARCHAR(100) PRIMARY KEY,
    language VARCHAR(100) NOT NULL
    );

CREATE TABLE IF NOT EXISTS movies_genre (
    movie_id VARCHAR(100) NOT NULL,
    genre_id VARCHAR(100) NOT NULL,
    PRIMARY KEY (movie_id, genre_id),
    CONSTRAINT fk_movie_genre_movie
    FOREIGN KEY (movie_id)
    REFERENCES movies(movie_id)
    ON DELETE CASCADE,
    CONSTRAINT fk_movie_genre_genre
    FOREIGN KEY (genre_id)
    REFERENCES genres(genre_id)
    );

CREATE TABLE IF NOT EXISTS movies_language (
    movie_id VARCHAR(100) NOT NULL,
    language_id VARCHAR(100) NOT NULL,
    PRIMARY KEY (movie_id, language_id),
    CONSTRAINT fk_movie_language_movie
    FOREIGN KEY (movie_id)
    REFERENCES movies(movie_id)
    ON DELETE CASCADE,
    CONSTRAINT fk_movie_language_language
    FOREIGN KEY (language_id)
    REFERENCES languages(language_id)
    );