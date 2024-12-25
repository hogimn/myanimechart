CREATE TABLE anime
(
    id            INT AUTO_INCREMENT PRIMARY KEY,
    title         VARCHAR(255),
    link          VARCHAR(255),
    image         VARCHAR(255),
    score         DOUBLE,
    members       INT,
    genre         VARCHAR(255),
    studios       VARCHAR(255),
    source        VARCHAR(255),
    season        VARCHAR(7),
    year          INT,
    rank          INT,
    popularity    INT,
    scoring_count INT,
    episodes      INT,
    air_status    VARCHAR(255)
);

CREATE TABLE anime_stat
(
    anime_id      INT,
    members       INT,
    score         DECIMAL(3, 1),
    scoring_count INT,
    rank          INT,
    popularity    INT,
    recorded_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (anime_id, recorded_at),
    FOREIGN KEY (anime_id) REFERENCES anime (id)
);