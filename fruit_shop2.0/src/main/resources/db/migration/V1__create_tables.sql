create table users (
                       id serial primary key,
                       username varchar(255) not null unique,
                       password varchar(255) not null,
                       role varchar(255)
);

create table fruit (
                       id serial primary key,
                       name varchar(255) not null unique,
                       description varchar(255),
                       price double precision check (price > 0),
                       user_id integer references users (id)
);