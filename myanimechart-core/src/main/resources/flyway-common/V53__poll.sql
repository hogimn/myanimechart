UPDATE batch
SET cron = '0 0 0 * * ?'
WHERE name IN ('AnimeCollectJob', 'PollCollectJob')
;