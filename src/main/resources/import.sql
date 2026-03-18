-- Authors
INSERT INTO author (id, firstname, lastname) VALUES (1, 'Victor', 'Hugo');
INSERT INTO author (id, firstname, lastname) VALUES (2, 'Albert', 'Camus');
INSERT INTO author (id, firstname, lastname) VALUES (3, 'J.K.', 'Rowling');
INSERT INTO author (id, firstname, lastname) VALUES (4, 'George', 'Orwell');
INSERT INTO author (id, firstname, lastname) VALUES (5, 'Frank', 'Herbert');
INSERT INTO author (id, firstname, lastname) VALUES (6, 'Agatha', 'Christie');

-- Genres
INSERT INTO genre (id, name) VALUES (1, 'Roman');
INSERT INTO genre (id, name) VALUES (2, 'Fantaisie');
INSERT INTO genre (id, name) VALUES (3, 'Science-Fiction');
INSERT INTO genre (id, name) VALUES (4, 'Policier');

-- Users
INSERT INTO users (id, username, email, password, active, createdat, roles) 
VALUES (1, 'otman', 'otman@bookswap.fr', '$2a$10$/MrdN02lbKlHVAAlwfuj5eESOgTiJXhOfIarXmn2.oqwTdPXoM/SC', true, NOW(), '{"USER"}');
INSERT INTO users (id, username, email, password, active, createdat, roles) 
VALUES (2, 'aminata', 'aminata@bookswap.fr', '$2a$10$/MrdN02lbKlHVAAlwfuj5eESOgTiJXhOfIarXmn2.oqwTdPXoM/SC', true, NOW(), '{"USER"}');
INSERT INTO users (id, username, email, password, active, createdat, roles) 
VALUES (3, 'admin', 'admin@bookswap.fr', '$2a$10$b.zG09pgcRUM.AwDVglLCutzNydNsZ7noEIQQdzy.16EwE68xBame', true, NOW(), '{"USER", "ADMIN"}');

-- Books
INSERT INTO book (id, isbn, title, description, publicationyear, createdby_id, createdat)
VALUES (1, '9782070408504', 'Les Misérables', 'Classique de la littérature française', 1862, 1, NOW());
INSERT INTO book (id, isbn, title, description, publicationyear, createdby_id, createdat)
VALUES (2, '9782070360245', 'L''Étranger', 'Récit existentialiste', 1942, 2, NOW());
INSERT INTO book (id, isbn, title, description, publicationyear, createdby_id, createdat)
VALUES (3, '9782070540906', 'Harry Potter à l''école des sorciers', 'Premier tome de la saga', 1997, 1, NOW());
INSERT INTO book (id, isbn, title, description, publicationyear, createdby_id, createdat)
VALUES (4, '9782070368227', '1984', 'Dystopie totalitaire', 1949, 1, NOW());
INSERT INTO book (id, isbn, title, description, publicationyear, createdby_id, createdat)
VALUES (5, '9782070405077', 'Dune', 'Chef-d''œuvre de la SF', 1965, 1, NOW());
INSERT INTO book (id, isbn, title, description, publicationyear, createdby_id, createdat)
VALUES (6, '9782702310984', 'Le Meurtre de Roger Ackroyd', 'Enquête d''Hercule Poirot', 1926, 2, NOW());

-- Book_Author
INSERT INTO book_author (book_id, authors_id) VALUES (1, 1), (2, 2), (3, 3), (4, 4), (5, 5), (6, 6);

-- Book_Genre
INSERT INTO book_genre (book_id, genres_id) VALUES (1, 1), (2, 1), (3, 2), (4, 3), (5, 3), (6, 4);

-- User_Book (Bibliothèques)
INSERT INTO user_book (id, user_id, book_id, status, condition, isavailableforexchange, isavailableforloan, addedat)
VALUES (1, 1, 1, 'OWNED', 'GOOD', false, false, NOW());
INSERT INTO user_book (id, user_id, book_id, status, condition, isavailableforexchange, isavailableforloan, addedat)
VALUES (2, 1, 4, 'OWNED', 'GOOD', true, false, NOW());
INSERT INTO user_book (id, user_id, book_id, status, condition, isavailableforexchange, isavailableforloan, addedat)
VALUES (3, 1, 5, 'OWNED', 'GOOD', true, false, NOW());

-- Aminata : L'Étranger (2), Roger Ackroyd (6) + Wishlist Dune (5)
INSERT INTO user_book (id, user_id, book_id, status, condition, isavailableforexchange, isavailableforloan, addedat)
VALUES (4, 2, 2, 'OWNED', 'WORN', false, true, NOW());
INSERT INTO user_book (id, user_id, book_id, status, condition, isavailableforexchange, isavailableforloan, addedat)
VALUES (5, 2, 6, 'OWNED', 'WORN', false, true, NOW());
INSERT INTO user_book (id, user_id, book_id, status, condition, isavailableforexchange, isavailableforloan, addedat)
VALUES (6, 2, 5, 'WISHLIST', 'NEW', false, false, NOW());

-- Exchange
INSERT INTO exchange (id, requester_id, owner_id, book_id, status, type, requested_at, updated_at)
VALUES (1, 2, 1, 3, 'PENDING', 'EXCHANGE', NOW(), NOW());

-- Reviews (author_id fait référence à l'utilisateur auteur de la critique dans votre dump)
INSERT INTO review (id, author_id, book_id, rating, comment, createdat)
VALUES (1, 1, 5, 5, 'Un classique incontournable.', NOW());
INSERT INTO review (id, author_id, book_id, rating, comment, createdat)
VALUES (2, 2, 2, 4, 'Très profond et marquant.', NOW());

-- Mise à jour des séquences (Hibernate / Quarkus utilisent un incrément de 50 par défaut)
SELECT setval('author_seq', (SELECT MAX(id) FROM author));
SELECT setval('book_seq', (SELECT MAX(id) FROM book));
SELECT setval('exchange_seq', (SELECT MAX(id) FROM exchange));
SELECT setval('genre_seq', (SELECT MAX(id) FROM genre));
SELECT setval('review_seq', (SELECT MAX(id) FROM review));
SELECT setval('user_book_seq', (SELECT MAX(id) FROM user_book));
SELECT setval('users_seq', (SELECT MAX(id) FROM users));