CREATE TABLE IF NOT EXISTS Transaction (
    transactionId SERIAL PRIMARY KEY,
    labelTransaction  VARCHAR(255),
    amount DOUBLE PRECISION NOT NULL,
    dateOfTransaction TIMESTAMP NOT NULL,
    transactionsType VARCHAR(10) NOT NULL CHECK (transactionsType IN ('Debit', 'Credit')),
    accountId INT REFERENCES Account(accountId)
);


INSERT INTO Transaction (labelTransaction, amount, dateOfTransaction, transactionsType, accountId) VALUES
('Salary', 100000, CURRENT_TIMESTAMP, 'Credit',3);

INSERT INTO Transaction (labelTransaction, amount, dateOfTransaction, transactionsType, accountId) VALUES
    ('Gift christmas', 50000, CURRENT_TIMESTAMP, 'Debit', 2);

INSERT INTO Transaction (labelTransaction, amount, dateOfTransaction, transactionsType, accountId) VALUES
('New shoes', 20000, CURRENT_TIMESTAMP, 'Debit', 1);

