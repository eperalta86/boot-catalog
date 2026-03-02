
ALTER TABLE platforms ADD COLUMN created_at TIMESTAMP;
ALTER TABLE platforms ADD COLUMN updated_at TIMESTAMP;
UPDATE platforms SET created_at = NOW(), updated_at = NOW();
ALTER TABLE platforms ALTER COLUMN created_at SET NOT NULL;
ALTER TABLE platforms ALTER COLUMN updated_at SET NOT NULL;

ALTER TABLE media_items ADD COLUMN created_at TIMESTAMP;
ALTER TABLE media_items ADD COLUMN updated_at TIMESTAMP;
UPDATE media_items SET created_at = NOW(), updated_at = NOW();
ALTER TABLE media_items ALTER COLUMN created_at SET NOT NULL;
ALTER TABLE media_items ALTER COLUMN updated_at SET NOT NULL;

ALTER TABLE media_images RENAME COLUMN uploaded_at TO created_at;
ALTER TABLE media_images ADD COLUMN updated_at TIMESTAMP;
UPDATE media_images SET updated_at = created_at;
ALTER TABLE media_images ALTER COLUMN updated_at SET NOT NULL;
