CREATE TABLE IF NOT EXISTS Account (
    Id VARCHAR(50) PRIMARY KEY,
    Name VARCHAR(50) NOT NULL ,
    balance DOUBLE PRECISION NOT NULL,
    currencyId INT REFERENCES Currency (currencyid),
    accountType VARCHAR(20) NOT NULL CHECK (accountType IN ('General', 'Cash', 'My Account', 'Credit Card'))
);


INSERT INTO Account (Name, balance, currencyId, accountType) VALUES
    ('General Account', 1000.00, 1, 'General');

INSERT INTO Account (Name, balance, currencyId, accountType) VALUES
    ( 'Cash Account', 500.50, 1, 'Cash');

INSERT INTO Account (Name, balance, currencyId, accountType) VALUES
    ( 'My Account', 200.00, 1, 'My Account');

INSERT INTO Account (Name, balance, currencyId, accountType) VALUES
    ('Credit Card Account', 0.00, 1, 'Credit Card');