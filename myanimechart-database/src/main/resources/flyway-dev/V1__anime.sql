CREATE TABLE anime
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    title      VARCHAR(255),
    link       VARCHAR(255),
    image      VARCHAR(255),
    score      DECIMAL(3, 1),
    members    INT,
    genre      VARCHAR(255),
    studios    VARCHAR(255),
    source     VARCHAR(255),
    season     VARCHAR(7),
    year       INT
);

CREATE TABLE anime_stat
(
    anime_id    INT,
    members     INT,
    score       DECIMAL(3, 1),
    recorded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (anime_id, recorded_at),
    FOREIGN KEY (anime_id) REFERENCES anime (id)
);