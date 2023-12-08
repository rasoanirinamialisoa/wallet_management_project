package org.example.repository;

import org.example.model.TransferHistoryEntry;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransferHistoryOperations {
    private final Connection connection;

    public TransferHistoryOperations(Connection connection) {
        this.connection = connection;
    }

    public List<TransferHistoryEntry> getTransferHistoryInDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        List<TransferHistoryEntry> transferHistories = new ArrayList<>();
        String sql = "SELECT DH.id AS debit_account_id, CH.id AS credit_account_id, DH.amount AS transfer_amount, TH.transfer_date AS transfer_date FROM TransferHistory TH JOIN Transaction DH ON TH.debit_transaction_id = DH.id JOIN Transaction CH ON TH.credit_transaction_id = CH.id WHERE TH.transfer_date BETWEEN ? AND ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setObject(1, startDate);
            preparedStatement.setObject(2, endDate);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int debitAccountId = resultSet.getInt("debit_account_id");
                    int creditAccountId = resultSet.getInt("credit_account_id");
                    BigDecimal transferAmount = resultSet.getBigDecimal("transfer_amount");
                    LocalDateTime transferDate = resultSet.getTimestamp("transfer_date").toLocalDateTime();

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
