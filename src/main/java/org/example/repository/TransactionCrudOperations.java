package org.example.repository;


import org.example.model.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
        String query = "INSERT INTO transaction (accountId, description, amount, date, type) VALUES (?, ?, ?, ?, ?)";
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                for (Transaction transaction : toSave) {
                    preparedStatement.setInt(1, transaction.getAccountId());
                    preparedStatement.setString(2, transaction.getDescription());
                    preparedStatement.setDouble(3, transaction.getAmount());
                    preparedStatement.setTimestamp(4, transaction.getDate());
                    preparedStatement.setString(5, transaction.getType());
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
        String query = "INSERT INTO transaction (accountId, description, amount, date, type) VALUES (?, ?, ?, ?, ?) RETURNING *";
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, toSave.getAccountId());
                preparedStatement.setString(2, toSave.getDescription());
                preparedStatement.setDouble(3, toSave.getAmount());
                preparedStatement.setTimestamp(4, toSave.getDate());
                preparedStatement.setString(5, toSave.getType());

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

    private Transaction mapResultSetToTransaction(ResultSet resultSet) throws SQLException {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(resultSet.getInt("transactionId"));
        transaction.setAccountId(resultSet.getInt("accountId"));
        transaction.setDescription(resultSet.getString("description"));
        transaction.setAmount(resultSet.getDouble("amount"));
        transaction.setDate(resultSet.getTimestamp("date"));
        transaction.setType(resultSet.getString("type"));
        return transaction;
    }
}

