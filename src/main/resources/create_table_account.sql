CREATE TABLE IF NOT EXISTS Account (
    accountId SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL ,
    balance DOUBLE PRECISION NOT NULL,
    accountType VARCHAR(20) CHECK (accountType IN ('Bank', 'Cash', 'Mobile money')),
    currencyId INT REFERENCES Currency (currencyId)
);

INSERT INTO Account (Name, balance, currencyId, accountType) VALUES
    ('Current account', 1000.00, 1, 'Mobile money');

INSERT INTO Account (Name, balance, currencyId, accountType) VALUES
    ( 'Current account', 500.50, 1, 'Cash');

INSERT INTO Account (Name, balance, currencyId, accountType) VALUES
    ( 'Savings account', 200.00, 1, 'Bank');

