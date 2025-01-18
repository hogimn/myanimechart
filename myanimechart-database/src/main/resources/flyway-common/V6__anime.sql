ALTER TABLE anime
    ADD COLUMN start_date TIMESTAMP,
    ADD COLUMN end_date TIMESTAMP,
    ADD COLUMN english_title VARCHAR(1000),
    ADD COLUMN japanese_title VARCHAR(1000);