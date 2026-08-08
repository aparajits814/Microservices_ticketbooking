CREATE TABLE IF NOT EXISTS payments (
    payment_id VARCHAR(100) PRIMARY KEY,
    booking_id VARCHAR(100) NOT NULL,
    payment_amount DECIMAL(10,2) NOT NULL,
    currency VARCHAR(3) NOT NULL DEFAULT 'INR',
    payment_status VARCHAR(30) NOT NULL,
    stripe_checkout_session_id VARCHAR(100),
    stripe_payment_intent VARCHAR(100),
    checkout_url VARCHAR(500),
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    version BIGINT NOT NULL DEFAULT 0,

    CONSTRAINT uk_checkout_session
    UNIQUE (stripe_checkout_session_id)
    );

CREATE TABLE IF NOT EXISTS payment_outbox (
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