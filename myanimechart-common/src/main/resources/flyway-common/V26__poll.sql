CREATE TABLE anime_keyword_mapping (
    anime_id BIGINT PRIMARY KEY,
    search_keyword VARCHAR(255) NOT NULL
);

CREATE TABLE anime_episode_topic_mapping (
    anime_id INT NOT NULL,
    episode INT NOT NULL,
    topic_id BIGINT NOT NULL,
    PRIMARY KEY (anime_id, episode, topic_id)
);

INSERT INTO anime_keyword_mapping (anime_id, search_keyword)
VALUES (12345, 'Touhai: Ura Rate Mahjong Touhai Roku Poll Episode Discussion');

INSERT INTO anime_keyword_mapping (anime_id, search_keyword)
VALUES (59868, 'Jib-i Eopseo Poll Episode Discussion');

INSERT INTO anime_keyword_mapping (anime_id, search_keyword)
VALUES (60048, 'BLEACH 20th Poll Episode Discussion');

INSERT INTO anime_episode_topic_mapping (anime_id, episode, topic_id)
VALUES (60338, 1, 2190682);