CREATE TABLE IF NOT EXISTS Currency (
    id SERIAL PRIMARY KEY ,
    currencyCode VARCHAR(3) NOT NULL,
    currencyName VARCHAR(50) NOT NULL
);