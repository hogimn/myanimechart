CREATE TABLE IF NOT EXISTS poll_collection_status
(
    anime_id     INT NOT NULL,
    status       ENUM('IN_PROGRESS', 'COMPLETED', 'FAILED', 'WAIT') NOT NULL,
    started_at   TIMESTAMP,
    finished_at  TIMESTAMP,
    updated_at   TIMESTAMP,
    PRIMARY KEY (anime_id)
);
