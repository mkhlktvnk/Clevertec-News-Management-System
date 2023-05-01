CREATE TABLE IF NOT EXISTS news
(
    id    BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    time  TIMESTAMP    NOT NULL DEFAULT NOW(),
    title VARCHAR(255) NOT NULL,
    text  TEXT         NOT NULL
);

CREATE TABLE IF NOT EXISTS comments
(
    id       BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    time     TIMESTAMP    NOT NULL DEFAULT NOW(),
    username VARCHAR(255) NOT NULL,
    text     TEXT         NOT NULL,
    news_id  BIGINT       NOT NULL REFERENCES news (id)
);
