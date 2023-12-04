-- Script pour créer la table Transaction
CREATE TABLE IF NOT EXISTS Transaction (
    transactionId SERIAL PRIMARY KEY,
    accountId VARCHAR(50) REFERENCES Account(Id)
    description VARCHAR(255),
    amount DOUBLE PRECISION NOT NULL,
    date TIMESTAMP NOT NULL,
    type VARCHAR(10) NOT NULL CHECK (type IN ('DEBIT', 'CREDIT')), 
);
