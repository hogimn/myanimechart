CREATE TABLE poll_option
(
    id   INT PRIMARY KEY,
    text VARCHAR(255) NOT NULL
);

INSERT INTO poll_option (id, text)
VALUES (5, '5 out of 5: Loved it!'),
       (4, '4 out of 5: Liked it'),
       (3, '3 out of 5: It was OK'),
       (2, '2 out of 5: Disliked it'),
       (1, '1 out of 5: Hated it');

CREATE TABLE poll
(
    anime_id   INT    NOT NULL,
    poll_id    INT    NOT NULL,
    topic_id   BIGINT NOT NULL,
    title      VARCHAR(255),
    episode    INT,
    votes      INT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    PRIMARY KEY (anime_id, poll_id, topic_id),
    FOREIGN KEY (anime_id) REFERENCES anime (id),
    FOREIGN KEY (poll_id) REFERENCES poll_option (id)
);
