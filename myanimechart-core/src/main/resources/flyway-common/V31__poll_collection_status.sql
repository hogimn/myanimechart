CREATE TABLE poll_collection_status (
    anime_id INT PRIMARY KEY,
    status ENUM('IN_PROGRESS', 'COMPLETED', 'FAILED') NOT NULL,
    started_at TIMESTAMP,
    finished_at TIMESTAMP,
    updated_at TIMESTAMP
);
