CREATE TABLE IF NOT EXISTS batch
(
    name VARCHAR(255),
    cron VARCHAR(255),
    PRIMARY KEY (name)
);

CREATE TABLE IF NOT EXISTS batch_history
(
    name        VARCHAR(255),
    recorded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (name, recorded_at)
);

INSERT INTO batch (name, cron)
VALUES
  ('AnimeCollectJob', '0 0 0 * * ?'),
  ('BatchMonitorJob', '0 * * * * ?'),
  ('PollCollectJob',  '0 0 0 * * ?')
ON DUPLICATE KEY UPDATE
  cron = VALUES(cron);
