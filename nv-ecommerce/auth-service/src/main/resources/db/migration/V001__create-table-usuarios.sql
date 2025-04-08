CREATE TABLE usuarios (
    id BINARY(16) NOT NULL, -- Armazena o UUID como binário (mais eficiente)
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL, -- Senha armazenada (hash)
    role ENUM('ADMIN', 'CLIENTE', 'GESTOR') NOT NULL,
    criado_em TIMESTAMP NOT NULL,
    atualizado_em TIMESTAMP,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,

    PRIMARY KEY (id)
);

INSERT INTO usuarios (id, nome, email, senha, role, criado_em, atualizado_em, ativo)
VALUES (
    UNHEX(REPLACE(UUID(), '-', '')), -- Gera um UUID único no formato binário
    'Administrador',
    'admin@email.com.br',
    '$2a$12$ObuRVwlDsjKpBNMGOkaUjeD4ESgRLLnMnug0EvnQ8n3JJLppTuani', -- Senha com hash (@Admin123)
    'ADMIN',
    NOW(),
    NOW(),
    TRUE
);