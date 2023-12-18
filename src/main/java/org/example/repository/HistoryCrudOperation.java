package org.example.repository;

import org.example.model.HistoryBalance;
import org.example.model.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HistoryCrudOperation {
    static Connection connection;

    public HistoryCrudOperation(Connection connection) {
        this.connection = connection;
    }

    public static List<HistoryBalance> findAll(String accountId, LocalDateTime startDate, LocalDateTime endDate) {
        List<HistoryBalance> balanceHistories = new ArrayList<>();

        // SQL pour obtenir l'historique du solde dans une plage de dates
        String query = "SELECT date, balance FROM BalanceHistory WHERE accountId = ? AND date BETWEEN ? AND ? ORDER BY date";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, Integer.parseInt(accountId));
            preparedStatement.setObject(2, startDate);
            preparedStatement.setObject(3, endDate);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    LocalDateTime date = resultSet.getTimestamp("date").toLocalDateTime();
                    Double balance = resultSet.getDouble("balance");

                    HistoryBalance entry = new HistoryBalance(date, balance);
                    balanceHistories.add(entry);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return balanceHistories;
    }

    private HistoryBalance mapResultSetToHistoryBalance(ResultSet resultSet) throws SQLException {
        HistoryBalance historyBalance = new HistoryBalance();
        historyBalance.setHistoryId(resultSet.getInt(historyBalance.HISTORY_ID));
        historyBalance.setBalance(resultSet.getDouble(historyBalance.BALANCE_HISTORY));
        historyBalance.setDate(resultSet.getTimestamp(historyBalance.DATE_HISTORY_BALANCE));
        return historyBalance;
    }

}
