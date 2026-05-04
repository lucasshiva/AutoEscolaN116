CREATE TABLE users
(
    id         BIGINT       NOT NULL AUTO_INCREMENT,
    login      VARCHAR(50)  NOT NULL UNIQUE,
    senha      VARCHAR(255) NOT NULL,
    role       VARCHAR(30)  NOT NULL DEFAULT 'USER',

    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP,

    PRIMARY KEY (id)
);