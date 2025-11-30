DELETE FROM anime_keyword_mapping
WHERE anime_id = '60048';

INSERT INTO anime_episode_topic_mapping (anime_id, episode, topic_id)
VALUES (60048, 1, 2182670);
