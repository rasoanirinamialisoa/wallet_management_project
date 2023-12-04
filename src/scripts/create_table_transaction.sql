CREATE TABLE IF NOT EXISTS Transaction (
    transactionId SERIAL PRIMARY KEY,
    accountId VARCHAR(50) REFERENCES Account(Id),
    description VARCHAR(255),
    amount DOUBLE PRECISION NOT NULL,
    date TIMESTAMP NOT NULL,
    type VARCHAR(10) NOT NULL CHECK (type IN ('DEBIT', 'CREDIT'))
);


INSERT INTO Transaction (accountId, description, amount, date, type) VALUES
('123456789', 'Purchase at XYZ Store', 50.00, CURRENT_TIMESTAMP, 'DEBIT');


INSERT INTO Transaction (accountId, description, amount, date, type) VALUES
('987654321', 'Salary Deposit', 2000.00, CURRENT_TIMESTAMP, 'CREDIT');


INSERT INTO Transaction (accountId, description, amount, date, type) VALUES
('555555555', 'Online Shopping', 75.50, CURRENT_TIMESTAMP, 'DEBIT');

