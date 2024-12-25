CREATE TABLE batch
(
    name VARCHAR(255),
    cron VARCHAR(255),
    PRIMARY KEY (name)
);

CREATE TABLE batch_history
(
    name        VARCHAR(255),
    recorded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (name, recorded_at),
    FOREIGN KEY (name) REFERENCES batch (name)
);

insert into batch (name, cron)
values ("AnimeCollectorJob", "0 0 * * * ?");
insert into batch (name, cron)
values ("BatchMonitorJob", "0 * * * * ?");
