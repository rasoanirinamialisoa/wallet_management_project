CREATE TABLE TransferHistory (
                                 ID SERIAL PRIMARY KEY,
                                 debit_transaction_id INT,
                                 credit_transaction_id INT,
                                 transfer_date DATETIME,
                                 FOREIGN KEY (debit_transaction_id) REFERENCES Transaction(id),
                                 FOREIGN KEY (credit_transaction_id) REFERENCES Transaction(id)
);

INSERT INTO TransferHistory (debit_transaction_id, credit_transaction_id, transfer_date)
VALUES (1, 2, '2023-12-01 12:00:00'),
       (3, 4, '2023-12-02 12:30:00'),
       (5, 6, '2023-12-03 14:45:00');
