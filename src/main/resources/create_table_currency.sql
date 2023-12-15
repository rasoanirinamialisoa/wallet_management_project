CREATE TABLE IF NOT EXISTS Currency (
    currencyId SERIAL PRIMARY KEY ,
    currencyCode VARCHAR(3) NOT NULL,
    currencyName VARCHAR(50) NOT NULL
);


INSERT INTO Currency (currencyCode, currencyName) VALUES
('EUR', 'Euro');

INSERT INTO Currency (currencyCode, currencyName) VALUES
('MGA', 'Ariary');
