CREATE TABLE produtos (
    id CHAR(36) PRIMARY KEY, -- UUID armazenado como string de 36 caracteres
    nome VARCHAR(100) NOT NULL, -- Nome do produto
    descricao VARCHAR(255) NOT NULL, -- Descrição do produto
    preco DECIMAL(10, 2) NOT NULL, -- Preço com duas casas decimais
    categoria ENUM('ELETRONICOS', 'ALIMENTOS', 'ROUPAS', 'LIVROS', 'MOVEIS', 'COSMETICOS', 'ESPORTES', 'SAUDE', 'BRINQUEDOS') NOT NULL, -- Categoria do produto
    estoque INT NOT NULL CHECK (estoque >= 0), -- Estoque (não pode ser negativo)
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Data e hora de criação
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP -- Data e hora de última atualização
);

