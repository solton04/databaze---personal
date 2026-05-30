create schema APP;

create user APP_USER password 'APP_USER';

grant all on schema APP to APP_USER;

use APP;



create table AUTHOR (
  ID bigint auto_increment not null,
  VERSION bigint not null default 0,
  FIRST_NAME varchar not null,
  LAST_NAME varchar not null,
  primary key (ID)
);

create table BOOK (
  ID bigint auto_increment not null,
  VERSION bigint not null default 0,
  AUTHOR_ID bigint not null,
  TITLE varchar not null,
  primary key (ID),
  foreign key (AUTHOR_ID) REFERENCES AUTHOR(ID)
);

create table LOAN (
  ID bigint auto_increment not null,
  VERSION bigint not null default 0,
  BOOK_ID bigint not null,
  BORROWER_NAME varchar not null,
  LOAN_DATE date not null,
  RETURN_DATE date,
  primary key (ID),
  foreign key (BOOK_ID) REFERENCES BOOK(ID)
);

INSERT INTO AUTHOR (FIRST_NAME, LAST_NAME) VALUES
('Karel', 'Čapek'),
('Božena', 'Němcová'),
('Franz', 'Kafka'),
('J.R.R.', 'Tolkien'),
('J.K.', 'Rowlingová'),
('George R.R.', 'Martin'),
('Agatha', 'Christie'),
('Stephen', 'King'),
('Isaac', 'Asimov'),
('Arthur C.', 'Clarke');



INSERT INTO BOOK (AUTHOR_ID, TITLE) VALUES
(1, 'R.U.R.'),
(1, 'Válka s Mloky'),
(1, 'Bílá nemoc'),
(1, 'Krakatit'),
(2, 'Babička'),
(2, 'Divá Bára'),
(3, 'Proměna'),
(3, 'Proces'),
(3, 'Zámek'),
(4, 'Společenstvo Prstenu'),
(4, 'Dvě věže'),
(4, 'Návrat krále'),
(4, 'Hobit aneb Cesta tam a zase zpátky'),
(5, 'Harry Potter a Kámen mudrců'),
(5, 'Harry Potter a Tajemná komnata'),
(5, 'Harry Potter a Vězeň z Azkabanu'),
(6, 'Hra o trůny'),
(6, 'Střet králů'),
(6, 'Bouře mečů'),
(7, 'Vražda v Orient expresu'),
(7, 'Deset malých černoušků'),
(7, 'Smrt na Nilu'),
(8, 'Osvícení'),
(8, 'To'),
(8, 'Zelená míle'),
(9, 'Nadace'),
(9, 'Nadace a Říše'),
(9, 'Druhá Nadace'),
(10, '2001: Vesmírná odysea'),
(10, 'Setkání s Rámou');



INSERT INTO LOAN (BOOK_ID, BORROWER_NAME, LOAN_DATE, RETURN_DATE) VALUES
(1, 'Jan Novák', '2023-01-15', '2023-02-14'),
(2, 'Petr Svoboda', '2023-02-01', '2023-02-28'),
(10, 'Jana Dvořáková', '2023-03-10', '2023-04-10'),
(14, 'Tomáš Kučera', '2023-03-15', '2023-03-30'),
(20, 'Eva Černá', '2023-04-01', '2023-04-15'),
(24, 'Martin Veselý', '2023-04-20', '2023-05-20'),
(30, 'Lucie Krejčí', '2023-05-05', '2023-06-05'),
(5, 'Jan Novák', '2023-05-10', '2023-06-10'),
(7, 'Pavel Horák', '2023-06-01', '2023-06-15'),
(17, 'Jana Dvořáková', '2023-06-15', '2023-07-15'),
(23, 'Klára Marková', '2023-07-01', '2023-08-01'),
(1, 'Tomáš Kučera', '2023-07-10', '2023-08-10'),
(11, 'Petr Svoboda', '2023-08-01', '2023-09-01'),
(15, 'Eva Černá', '2023-08-15', '2023-09-15'),
(21, 'Martin Veselý', '2023-09-01', '2023-10-01'),
(26, 'Lucie Krejčí', '2023-09-10', '2023-10-10'),
(8, 'Jan Novák', '2023-10-01', '2023-11-01'),
(12, 'Pavel Horák', '2023-10-15', '2023-11-15'),
(18, 'Jana Dvořáková', '2023-11-01', '2023-12-01'),
(25, 'Klára Marková', '2023-11-10', '2023-12-10'),
(3, 'Tomáš Kučera', '2023-12-01', '2024-01-01'),
(16, 'Petr Svoboda', '2023-12-15', '2024-01-15'),
(22, 'Eva Černá', '2024-01-01', '2024-02-01'),
(27, 'Martin Veselý', '2024-01-10', '2024-02-10'),
(9, 'Lucie Krejčí', '2024-02-01', '2024-03-01'),
(13, 'Jan Novák', '2024-02-15', '2024-03-15'),
(19, 'Pavel Horák', '2024-03-01', '2024-04-01'),
(29, 'Jana Dvořáková', '2024-03-10', '2024-04-10'),
(4, 'Klára Marková', '2024-04-01', NULL),
(6, 'Tomáš Kučera', '2024-04-15', NULL),
(28, 'Petr Svoboda', '2024-05-01', NULL),
(14, 'Eva Černá', '2024-05-10', NULL),
(17, 'Martin Veselý', '2024-05-20', NULL),
(23, 'Lucie Krejčí', '2024-06-01', NULL),
(2, 'Jan Novák', '2024-06-05', NULL),
(10, 'Pavel Horák', '2024-06-10', NULL),
(20, 'Jana Dvořáková', '2024-06-15', NULL),
(24, 'Klára Marková', '2024-06-20', NULL),
(30, 'Tomáš Kučera', '2024-06-25', NULL),
(5, 'Petr Svoboda', '2024-06-28', NULL);

commit;