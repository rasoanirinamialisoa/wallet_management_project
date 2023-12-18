package org.example.repository;


import org.example.model.BalanceSumsResult;
import org.example.model.Transaction;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        String query = "INSERT INTO transaction (accountId, labelTransaction, amount, dateOfTransaction, transactionsType, categoryId) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                for (Transaction transaction : toSave) {
                    preparedStatement.setInt(1, transaction.getAccountId());
                    preparedStatement.setString(2, transaction.getLabelTransaction());
                    preparedStatement.setDouble(3, transaction.getAmount());
                    preparedStatement.setTimestamp(4, transaction.getDateOfTransaction());
                    preparedStatement.setString(5, transaction.getTransactionsType());
                    preparedStatement.setInt(6, transaction.getCategoryId());
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
        String query = "INSERT INTO transaction (accountId, labelTransaction, amount, dateOfTransaction, transactionsType, categoryId) VALUES (?, ?, ?, ?, ?, ?) RETURNING *";
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, toSave.getAccountId());
                preparedStatement.setString(2, toSave.getLabelTransaction());
                preparedStatement.setDouble(3, toSave.getAmount());
                preparedStatement.setTimestamp(4, toSave.getDateOfTransaction());
                preparedStatement.setString(5, toSave.getTransactionsType());
                preparedStatement.setInt(6, toSave.getCategoryId());
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


    public BalanceSumsResult getBalanceSumsBetweenDates(int accountId, LocalDateTime startDate, LocalDateTime endDate) {
        CallableStatement callableStatement = null;

        try {
            String sql = "{ ? = call getbalancesumsbetweendates(accountId := ?, startDate := ?, endDate := ?) }";
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
            ResultSet resultSet = (ResultSet) callableStatement.getObject(1);

            // Mapper le résultat dans votre objet BalanceSumsResult
            BalanceSumsResult balanceSumsResult = new BalanceSumsResult();

            if (resultSet.next()) {
                balanceSumsResult.setTotalIncome(resultSet.getDouble("total_income"));
                balanceSumsResult.setTotalExpense(resultSet.getDouble("total_expense"));
            }

            return balanceSumsResult;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error executing stored procedure", e);
        } finally {
            // Assurez-vous de fermer la ressource CallableStatement dans le bloc finally
            if (callableStatement != null) {
                try {
                    callableStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public BigDecimal getCategorySumByIdAccount(String accountId, LocalDateTime startDate, LocalDateTime endDate) {
        BigDecimal restaurantSum = BigDecimal.ZERO;
        BigDecimal salarySum = BigDecimal.ZERO;

        String sql = "SELECT t.amount, c.categoryName " +
                "FROM Transaction t " +
                "LEFT JOIN Categories c ON t.categoryId = c.categoryId " +
                "WHERE t.accountId = ? AND t.dateOfTransaction BETWEEN ? AND ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, Integer.parseInt(accountId));
            preparedStatement.setObject(2, startDate);
            preparedStatement.setObject(3, endDate);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    BigDecimal amount = resultSet.getBigDecimal("amount");
                    String categoryName = resultSet.getString("name");
                    if ("Restaurant".equals(categoryName)) {
                        restaurantSum = restaurantSum.add(amount);
                        System.out.println("Restaurant sum : " + restaurantSum);
                    } else if ("Salaire".equals(categoryName)) {
                        salarySum = salarySum.add(amount);
                        System.out.println("Salaire sum :" + salarySum);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return restaurantSum.add(salarySum);
    }

    public BigDecimal getEntriesAndExitsSumByIdAccount(int accountId, LocalDateTime startDate, LocalDateTime endDate) {
        BigDecimal totalEntries = BigDecimal.ZERO;
        BigDecimal totalExits = BigDecimal.ZERO;

        String sql = "SELECT t.amount, t.transactionsType " +
                "FROM \"transaction\" t " +
                "WHERE t.accountId = ? AND t.dateOfTransaction BETWEEN ? AND ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, accountId);
            preparedStatement.setTimestamp(2, Timestamp.valueOf(startDate));
            preparedStatement.setTimestamp(3, Timestamp.valueOf(endDate));

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    BigDecimal amount = resultSet.getBigDecimal("amount");
                    String transactionType = resultSet.getString("transactionsType");
                    if ("credit".equals(transactionType)) {
                        totalEntries = totalEntries.add(amount);
                    } else if ("debit".equals(transactionType)) {
                        totalExits = totalExits.add(amount);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return totalEntries.add(totalExits);
    }


    private Transaction mapResultSetToTransaction(ResultSet resultSet) throws SQLException {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(resultSet.getInt(transaction.TRANSACTION_ID));
        transaction.setAccountId(resultSet.getInt(transaction.ACCOUNT_ID));
        transaction.setLabelTransaction(resultSet.getString(transaction.LABEL_TRANSACTION));
        transaction.setAmount(resultSet.getDouble(transaction.AMOUNT));
        transaction.setDateOfTransaction(resultSet.getTimestamp(transaction.DATE_OF_TRANSACTION));
        transaction.setTransactionsType(resultSet.getString(transaction.TRANSACTIONSTYPE));
        transaction.setCategoryId(resultSet.getInt(transaction.CATEGORY_ID));
        return transaction;
    }
}

