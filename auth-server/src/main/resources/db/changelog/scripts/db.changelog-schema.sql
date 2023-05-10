CREATE TABLE IF NOT EXISTS roles
(
    id        SERIAL PRIMARY KEY,
    authority VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS users
(
    id       SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS users_roles
(
    user_id INTEGER REFERENCES users (id) ON DELETE CASCADE,
    role_id INTEGER REFERENCES roles (id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);
