package org.example.repository;


import org.example.model.BalanceSumsResult;
import org.example.model.Transaction;

import java.sql.*;
import java.time.LocalDateTime;
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
        String query = "INSERT INTO transaction (accountId, labelTransaction, amount, dateOfTransaction, transactionsType, categoryId) VALUES (?, ?, ?, ?, ?)";
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                for (Transaction transaction : toSave) {
                    preparedStatement.setInt(1, transaction.getAccountId());
                    preparedStatement.setString(2, transaction.getLabelTransaction());
                    preparedStatement.setDouble(3, transaction.getAmount());
                    preparedStatement.setTimestamp(4, transaction.getDateOfTransaction());
                    preparedStatement.setString(5, transaction.getTransactionsType());
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
        String query = "INSERT INTO transaction (accountId, labelTransaction, amount, dateOfTransaction, transactionsType, categoryId) VALUES (?, ?, ?, ?, ?) RETURNING *";
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, toSave.getAccountId());
                preparedStatement.setString(2, toSave.getLabelTransaction());
                preparedStatement.setDouble(3, toSave.getAmount());
                preparedStatement.setTimestamp(4, toSave.getDateOfTransaction());
                preparedStatement.setString(5, toSave.getTransactionsType());

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


    public BalanceSumsResult getBalanceSumBetweenDates(int accountId, LocalDateTime startDate, LocalDateTime endDate) {
        CallableStatement callableStatement = null;

        try {
            String sql = "{ ? = call getBalanceSumBetweenDates(?, ?, ?) }";
            callableStatement = connection.prepareCall(sql);

            // Enregistrez le type de retour (OUT) pour la première variable
            callableStatement.registerOutParameter(1, Types.OTHER);

            // Paramètres d'entrée
            callableStatement.setInt(2, accountId);
            callableStatement.setTimestamp(3, Timestamp.valueOf(startDate));
            callableStatement.setTimestamp(4, Timestamp.valueOf(endDate));

            // Exécutez la procédure stockée
            callableStatement.execute();

            // Récupérez le résultat de la procédure stockée
            Object result = callableStatement.getObject(1);

            // Mapper le résultat dans votre objet BalanceSumsResult
            BalanceSumsResult balanceSumsResult = new BalanceSumsResult();

            if (result instanceof Struct) {
                Struct structResult = (Struct) result;
                Object[] attributes = structResult.getAttributes();

                // Supposons que la première valeur est total_income et la deuxième est total_expense
                balanceSumsResult.setTotalIncome((Double) attributes[0]);
                balanceSumsResult.setTotalExpense((Double) attributes[1]);
            }

            return balanceSumsResult;
        } catch (SQLException e) {
            // Gérer l'exception
            e.printStackTrace();
            throw new RuntimeException("Error executing stored procedure", e);
        } finally {
            // Fermez les ressources
            try {
                if (callableStatement != null) {
                    callableStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // Gérer l'exception
                e.printStackTrace();
            }
        }
    }



    private Transaction mapResultSetToTransaction(ResultSet resultSet) throws SQLException {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(resultSet.getInt("transactionId"));
        transaction.setAccountId(resultSet.getInt("accountId"));
        transaction.setLabelTransaction(resultSet.getString("labelTransaction"));
        transaction.setAmount(resultSet.getDouble("amount"));
        transaction.setDateOfTransaction(resultSet.getTimestamp("dateOfTransaction"));
        transaction.setTransactionsType(resultSet.getString("transactionsType"));
        transaction.setCategoryId(resultSet.getInt("categoryId"));
        return transaction;
    }
}

