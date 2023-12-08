CREATE TABLE IF NOT EXISTS BalanceHistory (
    id SERIAL PRIMARY KEY,
    id_account VARCHAR(50) REFERENCES Account(id),
    transaction_id INT REFERENCES Transaction(id),
    balance DECIMAL(18, 5) NOT NULL,
    date TIMESTAMP NOT NULL
);