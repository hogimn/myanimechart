UPDATE batch
SET cron = "0 0 */2 * * ?"
WHERE name = "PollCollectorJob";