-- Drop existing objects if they exist
DROP TABLE IF EXISTS ratings;
DROP TABLE IF EXISTS series;
DROP TABLE IF EXISTS users;
DROP VIEW IF EXISTS series_with_ratings;

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

-- Insert test data
-- Users
INSERT INTO users (id, username) VALUES
    ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'john_doe'),
    ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', 'jane_smith'),
    ('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13', 'bob_wilson'),
    ('d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a14', 'alice_brown');

-- Series (con IDs corregidos)
INSERT INTO series (id, title, streaming_platform, synopsis, cover_image_url) VALUES
    ('e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a15', 'Stranger Things', 'NETFLIX',
     'When a young boy vanishes, a small town uncovers a mystery involving secret experiments, terrifying supernatural forces and one strange little girl.',
     'https://placeholder.com/stranger-things.jpg'),
    
    ('f0eebc99-9c0b-4ef8-bb6d-6bb9bd380a16', 'The Mandalorian', 'DISNEY_PLUS',
     'After the stories of Jango and Boba Fett, another warrior emerges in the Star Wars universe.',
     'https://placeholder.com/mandalorian.jpg'),
    
    ('30eebc99-9c0b-4ef8-bb6d-6bb9bd380a17', 'The Boys', 'AMAZON_PRIME',
     'A group of vigilantes set out to take down corrupt superheroes who abuse their superpowers.',
     'https://placeholder.com/the-boys.jpg'),
    
    ('40eebc99-9c0b-4ef8-bb6d-6bb9bd380a18', 'House of the Dragon', 'HBO_MAX',
     'The story of the House Targaryen set 200 years before the events of Game of Thrones.',
     'https://placeholder.com/house-dragon.jpg');

-- Ratings (actualizados para usar los nuevos IDs de series)
INSERT INTO ratings (id, user_id, series_id, series_rating, created_at) VALUES
    ('20eebc99-9c0b-4ef8-bb6d-6bb9bd380a19', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a15', 9.0, CURRENT_TIMESTAMP),
    ('90eebc99-9c0b-4ef8-bb6d-6bb9bd380a20', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', 'e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a15', 8.5, CURRENT_TIMESTAMP),
    ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a21', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13', 'e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a15', 9.5, CURRENT_TIMESTAMP),
    
    ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a22', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'f0eebc99-9c0b-4ef8-bb6d-6bb9bd380a16', 8.0, CURRENT_TIMESTAMP),
    ('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a23', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', 'f0eebc99-9c0b-4ef8-bb6d-6bb9bd380a16', 8.5, CURRENT_TIMESTAMP),
    
    ('d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a24', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13', '30eebc99-9c0b-4ef8-bb6d-6bb9bd380a17', 9.0, CURRENT_TIMESTAMP),
    ('e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a25', 'd0eebc99-9c0b-4ef8-bb6d-6bb9bd380a14', '30eebc99-9c0b-4ef8-bb6d-6bb9bd380a17', 9.5, CURRENT_TIMESTAMP),
    
    ('f0eebc99-9c0b-4ef8-bb6d-6bb9bd380a26', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', '40eebc99-9c0b-4ef8-bb6d-6bb9bd380a18', 7.5, CURRENT_TIMESTAMP),
    ('10eebc99-9c0b-4ef8-bb6d-6bb9bd380a27', 'd0eebc99-9c0b-4ef8-bb6d-6bb9bd380a14', '40eebc99-9c0b-4ef8-bb6d-6bb9bd380a18', 8.0, CURRENT_TIMESTAMP);
