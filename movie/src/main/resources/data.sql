DELETE FROM movies;
DELETE FROM movies_language;
DELETE FROM movies_genre;
DELETE FROM genres;
DELETE FROM languages;
INSERT INTO genres (genre_id, genre_type) VALUES
('GEN001', 'Action'),
('GEN002', 'Adventure'),
('GEN003', 'Comedy'),
('GEN004', 'Drama'),
('GEN005', 'Sci-Fi'),
('GEN006', 'Thriller');
INSERT INTO languages (language_id, language) VALUES
('LANG001', 'English'),
('LANG002', 'Hindi'),
('LANG003', 'Tamil'),
('LANG004', 'Telugu');