CREATE TABLE IF NOT EXISTS poll_option
(
    id   INT PRIMARY KEY,
    text VARCHAR(255) NOT NULL
);

INSERT INTO poll_option (id, text)
VALUES
    (5, '5 out of 5: Loved it!'),
    (4, '4 out of 5: Liked it'),
    (3, '3 out of 5: It was OK'),
    (2, '2 out of 5: Disliked it'),
    (1, '1 out of 5: Hated it')
ON DUPLICATE KEY UPDATE
    text = VALUES(text);

CREATE TABLE IF NOT EXISTS poll
(
    anime_id        INT NOT NULL,
    poll_option_id  INT NOT NULL,
    topic_id        BIGINT NOT NULL,
    title           VARCHAR(255),
    episode         INT,
    votes           INT NOT NULL DEFAULT 0,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (anime_id, poll_option_id, topic_id)
);

CREATE INDEX IF NOT EXISTS idx_poll_option_id
    ON poll (poll_option_id);

CREATE INDEX IF NOT EXISTS idx_anime_poll_episode
    ON poll (anime_id, episode, poll_option_id);
