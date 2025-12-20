CREATE TABLE IF NOT EXISTS anime_keyword_mapping
(
    anime_id       BIGINT NOT NULL,
    search_keyword VARCHAR(255) NOT NULL,
    PRIMARY KEY (anime_id)
);

CREATE TABLE IF NOT EXISTS anime_episode_topic_mapping
(
    anime_id    INT NOT NULL,
    episode     INT NOT NULL,
    topic_id    BIGINT NOT NULL,
    PRIMARY KEY (anime_id, episode, topic_id)
);

CREATE INDEX IF NOT EXISTS idx_topic_lookup
    ON anime_episode_topic_mapping (topic_id);
