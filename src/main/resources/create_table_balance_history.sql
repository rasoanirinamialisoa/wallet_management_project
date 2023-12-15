CREATE TABLE IF NOT EXISTS BalanceHistory (
    id SERIAL PRIMARY KEY,
    balance DOUBLE PRECISION NOT NULL,
    date TIMESTAMP NOT NULL,
    accountId INT REFERENCES Account(accountId),
    transactionId INT REFERENCES Transaction(transactionId)
);

INSERT INTO BalanceHistory (balance, date, accountId, transactionId) VALUES
                                                                         (1000.0, '2023-01-01 12:00:00', 1, 41),
                                                                         (950.0, '2023-02-01 14:30:00', 1, 42),
                                                                         (1200.0, '2023-03-15 10:45:00', 2, 43),
                                                                         (800.0, '2023-04-05 16:20:00', 2, 44),
                                                                         (1500.0, '2023-05-20 08:00:00', 3, 45);
