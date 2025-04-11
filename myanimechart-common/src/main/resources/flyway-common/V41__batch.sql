UPDATE batch
SET cron = '0 0 */24 * * ?'
WHERE name = 'AnimeCollectJob';

UPDATE batch
SET cron = '0 0 */24 * * ?'
WHERE name = 'PollCollectJob';
