INSERT INTO users (id, username) 
VALUES ('550e8400-e29b-41d4-a716-446655440000', 'testUser');

INSERT INTO series (id, title, streaming_platform, synopsis) 
VALUES ('550e8400-e29b-41d4-a716-446655440001', 'Test Serie', 'NETFLIX', 'Test Synopsis');

INSERT INTO ratings (id, user_id, series_id, series_rating, created_at) 
VALUES (
    '550e8400-e29b-41d4-a716-446655440002',
    '550e8400-e29b-41d4-a716-446655440000',
    '550e8400-e29b-41d4-a716-446655440001',
    8.5,
    CURRENT_TIMESTAMP
);