CREATE TABLE TransferHistoryEntry (
                                 ID SERIAL PRIMARY KEY,
                                 debitTransactionId INT,
                                 creditTransactionId INT,
                                transferAmount DOUBLE PRECISION,
                                 transferDate TIMESTAMP,
                                FOREIGN KEY (debitTransactionId) REFERENCES Transaction(transactionId),
                                FOREIGN KEY (creditTransactionId) REFERENCES Transaction(transactionId)
);

INSERT INTO TransferHistoryEntry (debitTransactionId, creditTransactionId, transferAmount, transferDate)
VALUES (47, 43, 200000, '2023-12-01 12:00:00'),
       (48, 44, 40000000, '2023-12-02 12:30:00'),
       (49, 45, 9400000, '2023-12-03 14:45:00');



