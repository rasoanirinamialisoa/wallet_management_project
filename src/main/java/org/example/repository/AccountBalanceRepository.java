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

public class AccountBalanceRepository {
    private Connection connection;

    public AccountBalanceRepository(Connection connection) {
        this.connection = connection;
    }

    public BigDecimal getCurrentBalanceInAriary(int accountId) {
        BigDecimal currentBalance = BigDecimal.ZERO;

        // Obtenir tous les transferts liés au compte
        List<TransferHistoryEntry> transferHistories = getTransferHistoriesForAccount(accountId);

        // Parcourir chaque transfert
        for (TransferHistoryEntry transfer : transferHistories) {
            // Obtenir le taux de change au moment du transfert
            BigDecimal exchangeRate = getExchangeRate(transfer.getTransferDate(), transfer.getCreditAccountId());

            // Convertir le montant du transfert en Ariary
            BigDecimal transferAmountInAriary = transfer.getTransferAmount().multiply(exchangeRate);

            // Mettre à jour le solde actuel
            currentBalance = currentBalance.add(transferAmountInAriary);
        }

        return currentBalance;
    }

    private List<TransferHistoryEntry> getTransferHistoriesForAccount(int accountId) {
        List<TransferHistoryEntry> transferHistories = new ArrayList<>();
        String sql = "SELECT debit_account_id, credit_account_id, transfer_amount, transfer_date FROM TransferHistory WHERE debit_account_id = ? OR credit_account_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, accountId);
            preparedStatement.setInt(2, accountId);

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

    private BigDecimal getExchangeRate(LocalDateTime date, int currencyId) {
        // Remplacez "CurrencyValue" par le nom réel de votre table dans la base de données
        String sql = "SELECT value FROM CurrencyValue WHERE id_devise_source = ? AND date_effet <= ? ORDER BY date_effet DESC LIMIT 1";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, currencyId);
            preparedStatement.setObject(2, date);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // Récupérer la valeur du taux de change
                    return resultSet.getBigDecimal("value");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return BigDecimal.ONE;
    }
}

