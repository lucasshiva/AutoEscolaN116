CREATE TABLE students
(
    id                   BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    nome                 VARCHAR(150)                      NOT NULL,
    email                VARCHAR(150)                      NOT NULL,
    telefone             VARCHAR(15)                       NOT NULL,
    cpf                  VARCHAR(15)                       NOT NULL UNIQUE,

    endereco_logradouro  VARCHAR(255)                      NOT NULL,
    endereco_numero      VARCHAR(8),
    endereco_complemento VARCHAR(50),
    endereco_cidade      VARCHAR(100)                      NOT NULL,
    endereco_cep         VARCHAR(9)                        NOT NULL,
    endereco_uf          VARCHAR(2)                        NOT NULL,

    created_at           TIMESTAMP                         NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at           TIMESTAMP,
    deleted_at           TIMESTAMP
);