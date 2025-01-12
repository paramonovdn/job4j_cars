create table owners(
    id serial primary key,
    name varchar not null,
    user_id int not null REFERENCES auto_user(id)
);

create table history_owner(
    id serial primary key,
    owner_id int not null REFERENCES owners(id),
    car_id int not null REFERENCES car(id)
);