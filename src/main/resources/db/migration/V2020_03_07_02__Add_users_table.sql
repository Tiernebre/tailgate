CREATE TABLE users(
    id BIGSERIAL PRIMARY KEY,
    email CITEXT UNIQUE NOT NULL,
    password TEXT NOT NULL
);