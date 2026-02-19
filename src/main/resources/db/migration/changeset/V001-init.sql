CREATE TABLE IF NOT EXISTS users(
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE,
    password VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS cards(
    id SERIAL PRIMARY KEY,
    number VARCHAR(16) UNIQUE,
    expiry_date DATE,
    status VARCHAR(10),
    balance NUMERIC,
    user_id INT,

    CONSTRAINT fk_users
    FOREIGN KEY (user_id)
    REFERENCES users(id)

);