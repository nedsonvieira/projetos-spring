CREATE TABLE usuarios (

    id BIGINT AUTO_INCREMENT,
    login VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,

    PRIMARY KEY(id)
);