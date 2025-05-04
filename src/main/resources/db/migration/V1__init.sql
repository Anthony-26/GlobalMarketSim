CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password_hash VARCHAR(255) NOT NULL,
                       capital NUMERIC(19, 2) NOT NULL DEFAULT 100000.00
);

CREATE INDEX idx_users_email ON users(email);

CREATE TABLE assets (
                        id BIGSERIAL PRIMARY KEY,
                        name VARCHAR(255) NOT NULL UNIQUE,
                        type VARCHAR(50) NOT NULL,
                        price NUMERIC(19, 2) NOT NULL
);

CREATE TABLE transactions (
                              id BIGSERIAL PRIMARY KEY,
                              user_id BIGINT NOT NULL,
                              asset_id BIGINT NOT NULL,
                              order_type VARCHAR(50) NOT NULL,
                              quantity INTEGER NOT NULL,
                              price NUMERIC(19, 2) NOT NULL,
                              "timestamp" TIMESTAMP NOT NULL,
                              CONSTRAINT fk_transactions_user FOREIGN KEY (user_id) REFERENCES users(id),
                              CONSTRAINT fk_transactions_asset FOREIGN KEY (asset_id) REFERENCES assets(id)
);

CREATE INDEX idx_transactions_user_id ON transactions(user_id);
CREATE INDEX idx_transactions_asset_id ON transactions(asset_id);
CREATE INDEX idx_transactions_timestamp ON transactions("timestamp");

CREATE TABLE price_history (
                               id BIGSERIAL PRIMARY KEY,
                               asset_id BIGINT NOT NULL,
                               price NUMERIC(19, 2) NOT NULL,
                               "timestamp" TIMESTAMP NOT NULL,
                               CONSTRAINT fk_price_history_asset FOREIGN KEY (asset_id) REFERENCES assets(id)
);

CREATE INDEX idx_price_history_asset_id ON price_history(asset_id);
CREATE INDEX idx_price_history_timestamp ON price_history("timestamp");
CREATE INDEX idx_price_history_asset_timestamp ON price_history(asset_id, "timestamp");
