-- V1__Base_schema.sql

-- tabela de usuários
CREATE TABLE usuarios (
                          id SERIAL PRIMARY KEY,
                          nome VARCHAR(100) NOT NULL,
                          email VARCHAR(100) NOT NULL UNIQUE,
                          senha_hash VARCHAR(255) NOT NULL,
                          CONSTRAINT email_valido CHECK (POSITION('@' IN email) > 0)
);

-- tabela de gastos
CREATE TABLE gastos (
                        id SERIAL PRIMARY KEY,
                        usuario_id INT NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
                        nome VARCHAR(100) NOT NULL,
                        categoria VARCHAR(100),
                        valor DECIMAL(12,2) NOT NULL CHECK (valor >= 0),
                        data DATE NOT NULL
);

-- tabela de ganhos
CREATE TABLE ganhos (
                        id SERIAL PRIMARY KEY,
                        usuario_id INT NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
                        nome VARCHAR(100) NOT NULL,
                        categoria VARCHAR(100),
                        valor DECIMAL(12,2) NOT NULL CHECK (valor >= 0),
                        data DATE NOT NULL
);

-- tabela de renda atual
CREATE TABLE renda_atual (
                             id SERIAL PRIMARY KEY,
                             usuario_id INT NOT NULL UNIQUE REFERENCES usuarios(id) ON DELETE CASCADE,
                             renda_total DECIMAL(12,2) DEFAULT 0
);

-- função para atualizar a renda
CREATE OR REPLACE FUNCTION atualizar_renda()
RETURNS TRIGGER AS $$
BEGIN
UPDATE renda_atual
SET renda_total = (
    COALESCE((SELECT SUM(valor) FROM ganhos WHERE usuario_id = NEW.usuario_id), 0) -
    COALESCE((SELECT SUM(valor) FROM gastos WHERE usuario_id = NEW.usuario_id), 0)
    )
WHERE usuario_id = NEW.usuario_id;
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- triger para atualizar a renda após inserções ou exclusões em ganhos
CREATE TRIGGER tg_atualizar_renda_ganhos
    AFTER INSERT OR DELETE OR UPDATE ON ganhos
FOR EACH ROW
EXECUTE FUNCTION atualizar_renda();

-- triger para atualizar a renda após inserções ou exclusões em gastos
CREATE TRIGGER tg_atualizar_renda_gastos
    AFTER INSERT OR DELETE OR UPDATE ON gastos
FOR EACH ROW
EXECUTE FUNCTION atualizar_renda();

-- triger para criar uma linha de renda ao cadastrar o usuário
CREATE OR REPLACE FUNCTION criar_renda_usuario()
RETURNS TRIGGER AS $$
BEGIN
INSERT INTO renda_atual (usuario_id, renda_total) VALUES (NEW.id, 0);
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER tg_criar_renda_usuario
    AFTER INSERT ON usuarios
    FOR EACH ROW
    EXECUTE FUNCTION criar_renda_usuario();