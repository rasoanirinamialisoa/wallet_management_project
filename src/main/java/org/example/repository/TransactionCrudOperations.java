package org.example.repository;


import org.example.model.Account;
import org.example.model.Transaction;
import org.example.model.Currency;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.example.model.Transaction.*;

public class TransactionCrudOperations implements CrudOperations<Transaction> {

    private Connection connection;

    public TransactionCrudOperations(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Transaction> findAll() {
        List<Transaction> transactions = new ArrayList<>();
        try {
            String query = "SELECT * FROM transaction";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Transaction transaction = mapResultSetToTransaction(resultSet);
                    transactions.add(transaction);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    @Override
    public List<Transaction> saveAll(List<Transaction> toSave) {
        String query = "INSERT INTO transaction (accountId, labelTransaction, amount, dateOfTransaction, account) VALUES (?, ?, ?, ?, ?)";
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                for (Transaction transaction : toSave) {
                    preparedStatement.setInt(1, transaction.getTransactionId());
                    preparedStatement.setString(2, transaction.getLabelTransaction());
                    preparedStatement.setDouble(3, transaction.getAmount());
                    preparedStatement.setTimestamp(4, java.sql.Timestamp.valueOf(transaction.getDateOfTransaction()));
                    preparedStatement.setInt(5, transaction.getAccountId().getAccountId());
                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toSave;
    }

    @Override
    public Transaction save(Transaction toSave) {
        String query = "INSERT INTO transaction (labelTransaction, amount, dateOfTransaction, accountId) VALUES (?, ?, ?, ?, ?) RETURNING *";
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, toSave.getLabelTransaction());
                preparedStatement.setDouble(2, toSave.getAmount());
                preparedStatement.setObject(3, java.sql.Timestamp.valueOf(toSave.getDateOfTransaction()));
                preparedStatement.setInt(4, toSave.getAccountId().getAccountId());

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return mapResultSetToTransaction(resultSet);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Currency findById(int transactionId) {
        String query = "SELECT * FROM transaction WHERE " + TRANSACTION_ID_COLUMN + " = ?";
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, transactionId);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return mapResultSetToTransaction(resultSet).getAccountId().getCurrencyId();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Retourner null si aucune transaction n'est trouvée avec l'ID spécifié
    }
    private Transaction mapResultSetToTransaction(ResultSet resultSet) throws SQLException {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(resultSet.getInt(TRANSACTION_ID_COLUMN));
        transaction.setLabelTransaction(resultSet.getString(LABEL_COLUMN));
        transaction.setAmount(resultSet.getDouble(AMOUNT_COLUMN));
        transaction.setDateOfTransaction(resultSet.getTimestamp(DATE_COLUMN).toLocalDateTime());

        Account account = (Account) resultSet.getObject(ACCOUNT_COLUMN);
        transaction.setAccountId(account);


        return transaction;
    }
}

