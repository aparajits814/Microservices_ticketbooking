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