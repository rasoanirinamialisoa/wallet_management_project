CREATE TABLE IF NOT EXISTS Account (
    Id VARCHAR(50) PRIMARY KEY,
    Name VARCHAR(50) NOT NULL ,
    balance DOUBLE PRECISION NOT NULL,
    currencyCode VARCHAR(3) REFERENCES Currency(currencyCode)
    
);