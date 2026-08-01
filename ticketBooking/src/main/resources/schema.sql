CREATE TABLE IF NOT EXISTS booking (
    booking_id VARCHAR(100) PRIMARY KEY,
    user_id VARCHAR(100) NOT NULL,
    show_id VARCHAR(100) NOT NULL,
    booking_status VARCHAR(20) NOT NULL,
    payment_id VARCHAR(100),
    payment_amount INTEGER,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS booking_seats (
    booking_id VARCHAR(100) NOT NULL,
    seat_id VARCHAR(100) NOT NULL,
    PRIMARY KEY (booking_id, seat_id),
    CONSTRAINT fk_booking
    FOREIGN KEY (booking_id)
    REFERENCES booking(booking_id)
    ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS track_booking (
    booking_id VARCHAR(100) NOT NULL,
    payment_id VARCHAR(100) NOT NULL,
    PRIMARY KEY (booking_id,payment_id)
    );

CREATE TABLE IF NOT EXISTS booking_outbox (
    id VARCHAR(100) PRIMARY KEY,
    booking_id VARCHAR(100) NOT NULL,
    payment_id VARCHAR(100) NOT NULL,
    event_type VARCHAR(50) NOT NULL,
    topic VARCHAR(100) NOT NULL,
    payload TEXT NOT NULL,
    processed BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP,
    CONSTRAINT uk_booking_payment
    UNIQUE (booking_id, payment_id)
    );

CREATE TABLE IF NOT EXISTS idempotency_check(
    id VARCHAR(100) PRIMARY KEY,
    booking_id VARCHAR(100) NOT NULL,
    payment_id VARCHAR(100) NOT NULL,
    event_type VARCHAR(50) NOT NULL,
    CONSTRAINT uk_booking_payment
    UNIQUE (booking_id, payment_id, event_type)
    );