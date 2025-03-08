ALTER TABLE myanimechart.poll
ADD INDEX idx_anime_poll_episode (anime_id, poll_option_id, episode);
