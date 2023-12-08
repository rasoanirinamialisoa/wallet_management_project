package org.example.repository;
import models.BalanceHistory;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HistoryCrudOperation {
        private final Connection connection;
        public HistoryCrudOperation(Connection connection) {
        this.connection = connection;
        }
        public static List<BalanceHistory> getBalanceHistory (String accountId , LocalDateTime startDate ,LocalDateTime endDate ){
            List<BalanceHistory> balanceHistories = new ArrayList<>() ;
            String sql = "SELECT date, balance FROM BalanceHistory WHERE id_account = ? AND date BETWEEN ? AND ?";
            try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, accountId);
                preparedStatement.setObject(2, startDate);
                preparedStatement.setObject(3, endDate);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        LocalDateTime date = resultSet.getTimestamp("date").toLocalDateTime();
                        BigDecimal balance = resultSet.getBigDecimal("balance");

                        BalanceHistory entry = new BalanceHistory(date, balance);
                        balanceHistories.add(entry);
                    }
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return balanceHistories;
        }
}
