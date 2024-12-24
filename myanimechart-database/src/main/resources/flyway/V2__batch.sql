CREATE TABLE batch_history
(
    name        VARCHAR(255),
    recorded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (name, recorded_at)
);
