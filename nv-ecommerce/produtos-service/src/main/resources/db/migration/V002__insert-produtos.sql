-- Inserir produtos da categoria ELETRONICOS
INSERT INTO produtos (id, nome, descricao, preco, categoria, estoque)
VALUES
    (UUID(), 'Smartphone X1', 'Smartphone de última geração', 2999.99, 'ELETRONICOS', 50),
    (UUID(), 'Laptop Pro', 'Laptop leve e potente', 5999.99, 'ELETRONICOS', 20),
    (UUID(), 'Fone de Ouvido Bluetooth', 'Fone com cancelamento de ruído', 499.99, 'ELETRONICOS', 100),
    (UUID(), 'Smart TV 4K', 'Televisor 4K de 55 polegadas', 3999.99, 'ELETRONICOS', 15),
    (UUID(), 'Mouse Gamer RGB', 'Mouse com sensor de alta precisão', 249.99, 'ELETRONICOS', 80);

-- Inserir produtos da categoria ALIMENTOS
INSERT INTO produtos (id, nome, descricao, preco, categoria, estoque)
VALUES
    (UUID(), 'Arroz Integral', 'Arroz saudável de alta qualidade', 10.99, 'ALIMENTOS', 200),
    (UUID(), 'Biscoitos de Chocolate', 'Biscoito crocante com gotas de chocolate', 5.49, 'ALIMENTOS', 150),
    (UUID(), 'Suco Natural', 'Suco de laranja 100% natural', 12.99, 'ALIMENTOS', 100),
    (UUID(), 'Pasta de Amendoim', 'Pasta de amendoim integral', 19.99, 'ALIMENTOS', 70),
    (UUID(), 'Café Gourmet', 'Café premium com sabor intenso', 29.99, 'ALIMENTOS', 50);

-- Inserir produtos da categoria ROUPAS
INSERT INTO produtos (id, nome, descricao, preco, categoria, estoque)
VALUES
    (UUID(), 'Camiseta Básica', 'Camiseta confortável de algodão', 39.99, 'ROUPAS', 120),
    (UUID(), 'Calça Jeans', 'Calça jeans slim fit', 99.99, 'ROUPAS', 80),
    (UUID(), 'Jaqueta de Couro', 'Jaqueta de couro sintético', 199.99, 'ROUPAS', 30),
    (UUID(), 'Tênis Esportivo', 'Tênis leve e resistente para esportes', 249.99, 'ROUPAS', 50),
    (UUID(), 'Boné Unissex', 'Boné ajustável com design casual', 59.99, 'ROUPAS', 100);

-- Inserir produtos da categoria LIVROS
INSERT INTO produtos (id, nome, descricao, preco, categoria, estoque)
VALUES
    (UUID(), 'Livro de Ficção A', 'Uma emocionante história de ficção', 29.99, 'LIVROS', 70),
    (UUID(), 'Guia de Programação Java', 'Aprenda Java com exemplos práticos', 89.99, 'LIVROS', 40),
    (UUID(), 'História da Filosofia', 'Um panorama completo da filosofia ocidental', 59.99, 'LIVROS', 50),
    (UUID(), 'Livro Infantil B', 'História divertida para crianças', 19.99, 'LIVROS', 100),
    (UUID(), 'Manual de Cozinha', 'Receitas práticas e deliciosas', 49.99, 'LIVROS', 60);

-- Inserir produtos da categoria MOVEIS
INSERT INTO produtos (id, nome, descricao, preco, categoria, estoque)
VALUES
    (UUID(), 'Mesa de Escritório', 'Mesa de madeira com gavetas', 499.99, 'MOVEIS', 25),
    (UUID(), 'Cadeira Ergonômica', 'Cadeira confortável e ajustável', 699.99, 'MOVEIS', 30),
    (UUID(), 'Estante Modular', 'Estante para livros e decorações', 299.99, 'MOVEIS', 20),
    (UUID(), 'Guarda-Roupa Compacto', 'Guarda-roupa de 2 portas', 999.99, 'MOVEIS', 10),
    (UUID(), 'Sofá de 3 Lugares', 'Sofá confortável e elegante', 1599.99, 'MOVEIS', 5);

-- Inserir produtos da categoria COSMETICOS
INSERT INTO produtos (id, nome, descricao, preco, categoria, estoque)
VALUES
    (UUID(), 'Creme Hidratante', 'Creme hidratante para pele seca', 49.99, 'COSMETICOS', 100),
    (UUID(), 'Shampoo Anticaspa', 'Shampoo para cuidados com o couro cabeludo', 29.99, 'COSMETICOS', 150),
    (UUID(), 'Batom Matte', 'Batom com acabamento matte de longa duração', 19.99, 'COSMETICOS', 200),
    (UUID(), 'Perfume Floral', 'Fragrância floral suave', 89.99, 'COSMETICOS', 50),
    (UUID(), 'Kit de Maquiagem', 'Conjunto completo de maquiagem', 199.99, 'COSMETICOS', 30);

-- Inserir produtos da categoria ESPORTES
INSERT INTO produtos (id, nome, descricao, preco, categoria, estoque)
VALUES
    (UUID(), 'Bola de Futebol', 'Bola de futebol tamanho oficial', 99.99, 'ESPORTES', 80),
    (UUID(), 'Halteres Ajustáveis', 'Par de halteres com ajuste de peso', 249.99, 'ESPORTES', 40),
    (UUID(), 'Corda de Pular', 'Corda para exercícios aeróbicos', 29.99, 'ESPORTES', 200),
    (UUID(), 'Luvas de Boxe', 'Luvas acolchoadas para boxe', 149.99, 'ESPORTES', 60),
    (UUID(), 'Bicicleta Ergométrica', 'Equipamento para exercícios indoor', 1299.99, 'ESPORTES', 10);

-- Inserir produtos da categoria SAUDE
INSERT INTO produtos (id, nome, descricao, preco, categoria, estoque)
VALUES
    (UUID(), 'Vitamina C', 'Suplemento de vitamina C 500mg', 39.99, 'SAUDE', 120),
    (UUID(), 'Máscara Cirúrgica', 'Caixa com 50 máscaras descartáveis', 49.99, 'SAUDE', 500),
    (UUID(), 'Termômetro Digital', 'Termômetro para medições rápidas e precisas', 69.99, 'SAUDE', 70),
    (UUID(), 'Oxímetro', 'Oxímetro de dedo para medir oxigenação', 149.99, 'SAUDE', 40),
    (UUID(), 'Monitor de Pressão', 'Aparelho para medição de pressão arterial', 299.99, 'SAUDE', 30);

-- Inserir produtos da categoria BRINQUEDOS
INSERT INTO produtos (id, nome, descricao, preco, categoria, estoque)
VALUES
    (UUID(), 'Blocos de Montar', 'Conjunto de blocos coloridos para crianças', 89.99, 'BRINQUEDOS', 200),
    (UUID(), 'Boneca de Pano', 'Boneca artesanal de pano', 49.99, 'BRINQUEDOS', 150),
    (UUID(), 'Carro de Controle Remoto', 'Carro elétrico com controle remoto', 199.99, 'BRINQUEDOS', 60),
    (UUID(), 'Jogo de Tabuleiro', 'Jogo de tabuleiro para a família', 129.99, 'BRINQUEDOS', 80),
    (UUID(), 'Pelúcia de Animais', 'Pelúcia macia em formato de animal', 39.99, 'BRINQUEDOS', 100);