-- Drop existing objects if they exist
DROP VIEW IF EXISTS series_with_ratings;
DROP TABLE IF EXISTS ratings;
DROP TABLE IF EXISTS series;
DROP TABLE IF EXISTS users;

-- Create tables
CREATE TABLE users (
    id UUID PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE series (
    id UUID PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    streaming_platform VARCHAR(50) NOT NULL,
    synopsis TEXT,
    cover_image_url VARCHAR(255)
);

CREATE TABLE ratings (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL REFERENCES users(id),
    series_id UUID NOT NULL REFERENCES series(id),
    series_rating NUMERIC(3,1) NOT NULL CHECK (series_rating >= 0.0 AND series_rating <= 10.0),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, series_id)
);

-- Create indexes
CREATE INDEX idx_ratings_user_id ON ratings(user_id);
CREATE INDEX idx_ratings_series_id ON ratings(series_id);
CREATE INDEX idx_series_title ON series(title);

-- Create view
CREATE VIEW series_with_ratings AS
SELECT s.*, 
       COALESCE(FLOOR(AVG(r.series_rating) * 10) / 10, 0.0) as avg_rating
FROM series s
LEFT JOIN ratings r ON s.id = r.series_id
GROUP BY s.id;