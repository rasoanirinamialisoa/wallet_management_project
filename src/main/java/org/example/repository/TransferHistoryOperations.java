package org.example.repository;

import org.example.model.TransferHistoryEntry;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransferHistoryOperations {
    private static Connection connection = null;

    public TransferHistoryOperations(Connection connection) {
        this.connection = connection;
    }

    public static List<TransferHistoryEntry> findAll(LocalDateTime startDate, LocalDateTime endDate) {
        List<TransferHistoryEntry> transferHistories = new ArrayList<>();
        String query = "SELECT TH.debit_transaction_id AS debit_account_id, TH.credit_transaction_id AS credit_account_id, DH.amount AS transfer_amount, TH.transfer_date AS transfer_date FROM TransferHistoryEntry TH JOIN Transaction DH ON TH.debit_transaction_id = DH.transactionId JOIN Transaction CH ON TH.credit_transaction_id = CH.transactionId WHERE TH.transfer_date BETWEEN ? AND ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setObject(1, startDate);
            preparedStatement.setObject(2, endDate);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int debitAccountId = resultSet.getInt("debit_account_id");
                    int creditAccountId = resultSet.getInt("credit_account_id");
                    double transferAmount = resultSet.getDouble("transfer_amount");
                    Timestamp transferDate = Timestamp.valueOf(resultSet.getTimestamp("transfer_date").toLocalDateTime());

                    TransferHistoryEntry entry = new TransferHistoryEntry(debitAccountId, creditAccountId, transferAmount, transferDate);
                    transferHistories.add(entry);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return transferHistories;
    }
}
