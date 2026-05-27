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

commit;
