CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    login VARCHAR(255),
    password VARCHAR(255),
    nickname VARCHAR(255)
);