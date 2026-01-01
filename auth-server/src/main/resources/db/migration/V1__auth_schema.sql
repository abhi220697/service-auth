-- Enable UUID generation support
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- =========================
-- ROLES TABLE
-- =========================
CREATE TABLE roles (
                       id UUID PRIMARY KEY,
                       name VARCHAR(50) NOT NULL UNIQUE
);

-- =========================
-- AUTH USERS TABLE
-- =========================
CREATE TABLE auth_users (
                            id UUID PRIMARY KEY,
                            username VARCHAR(50) NOT NULL UNIQUE,
                            email VARCHAR(100) NOT NULL UNIQUE,
                            password VARCHAR(255) NOT NULL,
                            enabled BOOLEAN NOT NULL DEFAULT TRUE,
                            account_locked BOOLEAN NOT NULL DEFAULT FALSE,
                            created_at TIMESTAMP NOT NULL,
                            updated_at TIMESTAMP
);

-- =========================
-- USER ↔ ROLE JOIN TABLE
-- =========================
CREATE TABLE user_roles (
                            user_id UUID NOT NULL,
                            role_id UUID NOT NULL,
                            PRIMARY KEY (user_id, role_id),
                            CONSTRAINT fk_user_roles_user
                                FOREIGN KEY (user_id) REFERENCES auth_users(id) ON DELETE CASCADE,
                            CONSTRAINT fk_user_roles_role
                                FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

-- =========================
-- INDEXES (OPTIONAL BUT RECOMMENDED)
-- =========================
CREATE INDEX idx_auth_users_username ON auth_users(username);
CREATE INDEX idx_auth_users_email ON auth_users(email);
CREATE INDEX idx_user_roles_user_id ON user_roles(user_id);
CREATE INDEX idx_user_roles_role_id ON user_roles(role_id);

-- =========================
-- OPTIONAL SEED DATA (SAFE)
-- =========================
INSERT INTO roles (id, name)
VALUES (gen_random_uuid(), 'ROLE_ADMIN')
    ON CONFLICT DO NOTHING;

INSERT INTO roles (id, name)
VALUES (gen_random_uuid(), 'ROLE_USER')
    ON CONFLICT DO NOTHING;
