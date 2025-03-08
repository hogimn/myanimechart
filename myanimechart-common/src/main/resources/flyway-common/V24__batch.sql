UPDATE batch
SET cron = '0 0 */12 * * ?'
WHERE name = 'AnimeCollectJob';

UPDATE batch
SET cron = '0 30 */12 * * ?'
WHERE name = 'PollCollectJob';
