CREATE TABLE participates (
   id serial PRIMARY KEY,
   user_id int not null REFERENCES items(id),
   post_id int not null REFERENCES j_user(id),
   UNIQUE (user_id, post_id)
);