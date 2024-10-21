-- 1. Criar a tabela 'users'
CREATE TABLE IF NOT EXISTS users
(
    id              SERIAL PRIMARY KEY,
    username        VARCHAR(50)                 NOT NULL UNIQUE,
    email           VARCHAR(100)                NOT NULL UNIQUE,
    password        VARCHAR(255),
    cognito_user_id VARCHAR(255)                NOT NULL UNIQUE,
    created_at      TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW()
);

-- 2. Função para atualizar automaticamente o campo 'updated_at'
CREATE OR REPLACE FUNCTION update_updated_at_column()
    RETURNS TRIGGER AS
$$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- 3. Gatilho para chamar a função acima antes de atualizar um registro na tabela 'users'
CREATE TRIGGER trigger_update_updated_at
    BEFORE UPDATE
    ON users
    FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();
