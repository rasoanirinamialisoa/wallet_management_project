package org.example.repository;

import org.example.model.HistoryBalance;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HistoryCrudOperation {
    private static Connection connection;

    public HistoryCrudOperation(Connection connection) {
        this.connection = connection;
    }

    public static List<HistoryBalance> getBalanceHistory(String accountId, LocalDateTime startDate, LocalDateTime endDate) {
        List<HistoryBalance> balanceHistories = new ArrayList<>();

        // SQL pour obtenir l'historique du solde dans une plage de dates
        String sql = "SELECT date, balance FROM BalanceHistory WHERE id_account = ? AND date BETWEEN ? AND ? ORDER BY date";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, accountId);
            preparedStatement.setObject(2, startDate);
            preparedStatement.setObject(3, endDate);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    LocalDateTime date = resultSet.getTimestamp("date").toLocalDateTime();
                    BigDecimal balance = resultSet.getBigDecimal("balance");

                    HistoryBalance entry = new HistoryBalance(date, balance);
                    balanceHistories.add(entry);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return balanceHistories;
    }

    public static List<HistoryBalance> getBalanceHistoryInDateRange(String accountId, LocalDateTime startDate, LocalDateTime endDate) {
        List<HistoryBalance> balanceHistories = new ArrayList<>();

        // SQL pour obtenir l'historique du solde dans une plage de dates
        String sql = "SELECT date, balance FROM BalanceHistory WHERE id_account = ? AND date BETWEEN ? AND ? ORDER BY date";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, accountId);
            preparedStatement.setObject(2, startDate);
            preparedStatement.setObject(3, endDate);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    LocalDateTime date = resultSet.getTimestamp("date").toLocalDateTime();
                    BigDecimal balance = resultSet.getBigDecimal("balance");

                    HistoryBalance entry = new HistoryBalance(date, balance);
                    balanceHistories.add(entry);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return balanceHistories;
    }
}
