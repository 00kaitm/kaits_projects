-- insert into users(username, password) values('henryg', '123');
-- insert into users(username, password) values('kaitlynm', '321');
-- insert into users(username, password) values('fentrym', '456');
-- insert into users(username, password) values('ayanw', '654');
insert into users(username, password, role) values('henryg', '123', 'ADMIN');
insert into users(username, password, role) values('kaitlynm', '321', 'ADMIN');
insert into users(username, password, role) values('fentrym', '456', 'BASIC_USER');
insert into users(username, password, role) values('ayanw', '654', 'BASIC_USER');

insert into fruit(name, description, price, user_id) values ('Apple', 'Red delicious', 2.5, 1);
insert into fruit(name, description, price, user_id) values ('Strawberry', 'Seedy goodness', .5, 1);
insert into fruit(name, description, price, user_id) values ('Mango', 'Tropical', 2, 2);
insert into fruit(name, description, price, user_id) values ('Lime', 'Sour',  1, 2);
insert into fruit(name, description, price, user_id) values ('Watermelon', 'Nice Summer treat',  6, 3);
insert into fruit(name, description, price, user_id) values ('Orange', 'Citrus',  2, 4);

