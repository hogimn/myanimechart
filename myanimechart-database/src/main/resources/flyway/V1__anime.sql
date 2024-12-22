CREATE TABLE anime
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    title      VARCHAR(255),
    link       VARCHAR(255),
    image      VARCHAR(255),
    synopsis   TEXT,
    start_date DATE,
    score      DECIMAL(3, 1),
    members    INT,
    genre      VARCHAR(255),
    studios    VARCHAR(255),
    sources    VARCHAR(255),
    season     VARCHAR(7),
    year       INT
);

CREATE TABLE anime_stat
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    anime_id    INT,
    members     INT,
    score       DECIMAL(3, 1),
    recorded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (anime_id) REFERENCES anime (id)
);