UPDATE batch
SET name = "AnimeCollectJob"
WHERE name = "AnimeCollectorJob";

UPDATE batch
SET name = "PollCollectJob"
WHERE name = "PollCollectorJob";

UPDATE batch
SET cron = "0 0 */1 * * ?"
WHERE name = "AnimeCollectJob";

UPDATE batch
SET cron = "0 30 */1 * * ?"
WHERE name = "PollCollectJob";
