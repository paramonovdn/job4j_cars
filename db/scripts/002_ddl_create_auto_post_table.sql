create table auto_post
(
    id              serial primary key,
    description     varchar not null,
    created         timestamp,
    auto_user_id    int REFERENCES auto_user (id)  not null
);