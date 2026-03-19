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

-- Users (password hash for 'password123')
INSERT INTO users (id, username, email, password, active, createdat, roles) VALUES (1, 'otman', 'otman@example.com', '$2a$10$/MrdN02lbKlHVAAlwfuj5eESOgTiJXhOfIarXmn2.oqwTdPXoM/SC', true, NOW(), 'USER');
INSERT INTO users (id, username, email, password, active, createdat, roles) VALUES (2, 'aminata', 'aminata@example.com', '$2a$10$/MrdN02lbKlHVAAlwfuj5eESOgTiJXhOfIarXmn2.oqwTdPXoM/SC', true, NOW(), 'USER');

-- Books
INSERT INTO book (id, isbn, title, description, publicationyear, createdby_id, createdat) VALUES (1, '9782070408504', 'Les Miserables', 'Classique de la litterature francaise', 1862, 1, NOW());
INSERT INTO book (id, isbn, title, description, publicationyear, createdby_id, createdat) VALUES (2, '9782070360245', 'L''Etranger', 'Recit existentialiste', 1942, 2, NOW());
INSERT INTO book (id, isbn, title, description, publicationyear, createdby_id, createdat) VALUES (3, '9782070540906', 'Harry Potter', 'Premier tome de la saga', 1997, 1, NOW());
INSERT INTO book (id, isbn, title, description, publicationyear, createdby_id, createdat) VALUES (4, '9782070368227', '1984', 'Dystopie totalitaire', 1949, 1, NOW());
INSERT INTO book (id, isbn, title, description, publicationyear, createdby_id, createdat) VALUES (5, '9782070405077', 'Dune', 'Chef-d''oeuvre de la SF', 1965, 1, NOW());
INSERT INTO book (id, isbn, title, description, publicationyear, createdby_id, createdat) VALUES (6, '9782702310984', 'Le Meurtre de Roger Ackroyd', 'Enquete d''Hercule Poirot', 1926, 2, NOW());

-- Book_Author
INSERT INTO book_author (book_id, authors_id) VALUES (1, 1);
INSERT INTO book_author (book_id, authors_id) VALUES (2, 2);
INSERT INTO book_author (book_id, authors_id) VALUES (3, 3);
INSERT INTO book_author (book_id, authors_id) VALUES (4, 4);
INSERT INTO book_author (book_id, authors_id) VALUES (5, 5);
INSERT INTO book_author (book_id, authors_id) VALUES (6, 6);

-- Book_Genre
INSERT INTO book_genre (book_id, genres_id) VALUES (1, 1);
INSERT INTO book_genre (book_id, genres_id) VALUES (2, 1);
INSERT INTO book_genre (book_id, genres_id) VALUES (3, 2);
INSERT INTO book_genre (book_id, genres_id) VALUES (4, 3);
INSERT INTO book_genre (book_id, genres_id) VALUES (5, 3);
INSERT INTO book_genre (book_id, genres_id) VALUES (6, 4);

-- User_Book (Bibliotheques)
INSERT INTO user_book (id, user_id, book_id, status, condition, isavailableforexchange, isavailableforloan, addedat) VALUES (1, 1, 1, 'OWNED', 'GOOD', false, false, NOW());
INSERT INTO user_book (id, user_id, book_id, status, condition, isavailableforexchange, isavailableforloan, addedat) VALUES (2, 1, 4, 'OWNED', 'GOOD', true, false, NOW());
INSERT INTO user_book (id, user_id, book_id, status, condition, isavailableforexchange, isavailableforloan, addedat) VALUES (3, 1, 5, 'OWNED', 'GOOD', true, false, NOW());
INSERT INTO user_book (id, user_id, book_id, status, condition, isavailableforexchange, isavailableforloan, addedat) VALUES (4, 2, 2, 'OWNED', 'WORN', false, true, NOW());
INSERT INTO user_book (id, user_id, book_id, status, condition, isavailableforexchange, isavailableforloan, addedat) VALUES (5, 2, 6, 'OWNED', 'WORN', false, true, NOW());
INSERT INTO user_book (id, user_id, book_id, status, condition, isavailableforexchange, isavailableforloan, addedat) VALUES (6, 2, 5, 'WISHLIST', 'NEW', false, false, NOW());

-- Exchange
INSERT INTO exchange (id, requester_id, owner_id, book_id, status, type, requested_at, updated_at) VALUES (1, 2, 1, 3, 'PENDING', 'EXCHANGE', NOW(), NOW());

-- Reviews
INSERT INTO review (id, author_id, book_id, rating, comment, createdat) VALUES (1, 1, 5, 5, 'Un classique incontournable.', NOW());
INSERT INTO review (id, author_id, book_id, rating, comment, createdat) VALUES (2, 2, 2, 4, 'Tres profond et marquant.', NOW());

-- Advance sequences past inserted IDs (H2 compatible, replaces PostgreSQL setval)
ALTER SEQUENCE author_seq RESTART WITH 100;
ALTER SEQUENCE book_seq RESTART WITH 100;
ALTER SEQUENCE exchange_seq RESTART WITH 100;
ALTER SEQUENCE genre_seq RESTART WITH 100;
ALTER SEQUENCE review_seq RESTART WITH 100;
ALTER SEQUENCE user_book_seq RESTART WITH 100;
ALTER SEQUENCE users_seq RESTART WITH 100;
