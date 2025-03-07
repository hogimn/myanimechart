UPDATE batch
SET cron = "0 0 */1 * * ?"
WHERE name = "AnimeCollectorJob";

UPDATE batch
SET cron = "0 30 */1 * * ?"
WHERE name = "PollCollectorJob";
