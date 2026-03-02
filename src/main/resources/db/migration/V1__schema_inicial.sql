CREATE TABLE platforms (
    id          BIGSERIAL       PRIMARY KEY,
    name        VARCHAR(255)    NOT NULL UNIQUE,
    short_name  VARCHAR(255)    NOT NULL UNIQUE
);

CREATE TABLE media_items (
    id           BIGSERIAL       PRIMARY KEY,
    title        VARCHAR(255)    NOT NULL,
    platform_id  BIGINT          NOT NULL REFERENCES platforms(id),
    status       VARCHAR(50),
    release_date DATE
);

CREATE INDEX idx_media_items_platform ON media_items(platform_id);

CREATE TABLE media_images (
    id                 BIGSERIAL       PRIMARY KEY,
    image_type         VARCHAR(50)     NOT NULL,
    file_path          VARCHAR(1024)   NOT NULL,
    original_file_name VARCHAR(255),
    file_size          BIGINT,
    content_type       VARCHAR(100),
    uploaded_at        TIMESTAMP       NOT NULL,
    media_item_id      BIGINT          NOT NULL REFERENCES media_items(id) ON DELETE CASCADE
);

CREATE INDEX idx_media_images_media_item ON media_images(media_item_id);
