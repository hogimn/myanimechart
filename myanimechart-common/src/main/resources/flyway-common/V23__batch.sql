DELETE FROM batch WHERE name IN ('AnimeCollectorJob', 'PollCollectorJob');

INSERT INTO batch (name, cron)
VALUES
  ('AnimeCollectJob', '0 0 */1 * * ?'),
  ('PollCollectJob', '0 30 */1 * * ?');
