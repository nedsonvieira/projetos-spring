CREATE TABLE videos(

    id BIGINT NOT NULL AUTO_INCREMENT,
    titulo VARCHAR(60) NOT NULL,
    descricao VARCHAR(255),
    url VARCHAR(100) NOT NULL UNIQUE,
    ativo TINYINT(1) DEFAULT 1,

    PRIMARY KEY(id)
);