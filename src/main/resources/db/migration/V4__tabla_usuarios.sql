CREATE TABLE users (
    id         BIGSERIAL     PRIMARY KEY,
    username   VARCHAR(50)   NOT NULL UNIQUE,
    password   VARCHAR(255)  NOT NULL,
    enabled    BOOLEAN       NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP     NOT NULL,
    updated_at TIMESTAMP     NOT NULL
);

-- Usuario admin pre-configurado (password: admin123)
INSERT INTO users (username, password, enabled, created_at, updated_at)
VALUES ('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', TRUE, NOW(), NOW());