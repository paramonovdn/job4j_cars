create table engine(
    id serial primary key,
    name varchar not null
);

create table car(
    id serial primary key,
    name varchar not null,
    engine_id int not null REFERENCES engine(id)
);