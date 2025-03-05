ALTER TABLE videos ADD COLUMN categoria_id BIGINT;
ALTER TABLE categorias ADD COLUMN ativo TINYINT(1) DEFAULT 1;

ALTER TABLE videos ADD CONSTRAINT fk_categoria_id
FOREIGN KEY (categoria_id) REFERENCES categorias(id);