UPDATE batch
SET cron = "0 15 * * * ?"
WHERE name = "PollCollectorJob";
