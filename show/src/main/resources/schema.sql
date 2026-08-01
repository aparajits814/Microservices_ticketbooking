CREATE TABLE IF NOT EXISTS theatres (
    theatre_id VARCHAR(100) PRIMARY KEY,
    theatre_name VARCHAR(100) NOT NULL,
    location VARCHAR(255) NOT NULL,
    theatre_type CHAR(1) NOT NULL
    );

CREATE TABLE IF NOT EXISTS screens (
    screen_id VARCHAR(100) PRIMARY KEY,
    theatre_id VARCHAR(100) NOT NULL,
    screen_name VARCHAR(100) NOT NULL,
    screen_status CHAR(1) NOT NULL,

    CONSTRAINT fk_screens_theatre
    FOREIGN KEY (theatre_id)
    REFERENCES theatres(theatre_id)
    ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS seats (
    seat_id VARCHAR(100) PRIMARY KEY,
    screen_id VARCHAR(100) NOT NULL,
    seat_type CHAR(1) NOT NULL,

    CONSTRAINT fk_seats_screen
    FOREIGN KEY (screen_id)
    REFERENCES screens(screen_id)
    ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS shows (
    show_id VARCHAR(100) PRIMARY KEY,
    movie_id VARCHAR(100) NOT NULL,
    screen_id VARCHAR(100) NOT NULL,
    show_start_time TIMESTAMP NOT NULL,
    show_end_time TIMESTAMP NOT NULL,
    show_status CHAR(1) NOT NULL,

    CONSTRAINT fk_shows_screen
    FOREIGN KEY (screen_id)
    REFERENCES screens(screen_id)
    ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS show_pricing (
    show_id VARCHAR(100) NOT NULL,
    seat_type CHAR(1) NOT NULL,
    price DECIMAL(10,2) NOT NULL,

    PRIMARY KEY (show_id, seat_type),

    CONSTRAINT fk_show_pricing_show
    FOREIGN KEY (show_id)
    REFERENCES shows(show_id)
    ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS show_seats (
   show_id VARCHAR(100) NOT NULL,
   seat_id VARCHAR(100) NOT NULL,
   seat_status VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE',
   locked_by_booking_id VARCHAR(100),
   lock_expiry TIMESTAMP,
   version BIGINT NOT NULL DEFAULT 0,

   PRIMARY KEY (show_id, seat_id),

   CONSTRAINT fk_show_seats_show
       FOREIGN KEY (show_id)
           REFERENCES shows(show_id)
           ON DELETE CASCADE,

   CONSTRAINT fk_show_seats_seat
       FOREIGN KEY (seat_id)
           REFERENCES seats(seat_id)
           ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS seat_outbox (
    id VARCHAR(100) PRIMARY KEY,
    booking_id VARCHAR(100) NOT NULL,
    payment_id VARCHAR(100) NOT NULL,
    event_type VARCHAR(50) NOT NULL,
    topic VARCHAR(100) NOT NULL,
    payload TEXT NOT NULL,
    processed BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP,
    CONSTRAINT uk_booking_payment
    UNIQUE (booking_id, payment_id, event_type)
    );

CREATE TABLE IF NOT EXISTS idempotency_check(
    id VARCHAR(100) PRIMARY KEY,
    booking_id VARCHAR(100) NOT NULL,
    payment_id VARCHAR(100) NOT NULL,
    event_type VARCHAR(50) NOT NULL,
    CONSTRAINT uk_booking_payment
    UNIQUE (booking_id, payment_id, event_type)
    );