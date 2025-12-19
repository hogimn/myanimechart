CREATE TABLE IF NOT EXISTS api_call_log
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    endpoint     VARCHAR(255) NOT NULL,
    method       VARCHAR(10) NOT NULL,
    ip           VARCHAR(45) NOT NULL,
    country      VARCHAR(100),
    recorded_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_api_call_log_recorded_at
    ON api_call_log (recorded_at);

CREATE INDEX IF NOT EXISTS idx_api_call_log_endpoint_method
    ON api_call_log (endpoint, method);
