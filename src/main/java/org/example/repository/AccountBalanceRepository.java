package org.example.repository;

import org.example.model.TransferHistoryEntry;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AccountBalanceRepository {
    private static Connection connection;

    public AccountBalanceRepository(Connection connection) {
        this.connection = connection;
    }

    public BigDecimal getCurrentBalanceInAriary(int accountId) {
        BigDecimal currentBalance = BigDecimal.ZERO;

        try {
            connection.setAutoCommit(false);

            // Obtenir tous les transferts liés au compte
            List<TransferHistoryEntry> transferHistories = findAll(accountId);

            // Parcourir chaque transfert
            for (TransferHistoryEntry transfer : transferHistories) {
                // Obtenir le taux de change au moment du transfert
                BigDecimal exchangeRate = BigDecimal.valueOf(getExchangeRate(transfer.getTransferDate().toLocalDateTime(), transfer.getCreditTransactionId()));

                // Convertir le montant du transfert en Ariary
                BigDecimal transferAmountInAriary = BigDecimal.valueOf(transfer.getTransferAmount()).multiply(exchangeRate);

                // Mettre à jour le solde actuel
                currentBalance = currentBalance.add(transferAmountInAriary);
            }

            connection.commit();
        } catch (SQLException e) {
            // Gérer l'exception
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                // Gérer l'exception
            }
        }

        return currentBalance;
    }

    public static List<TransferHistoryEntry> findAll(int accountId) {
        List<TransferHistoryEntry> transferHistories = new ArrayList<>();
        String query = "SELECT debitTransactionId, creditTransactionId, transferAmount, transferDate FROM TransferHistoryEntry WHERE debitTransactionId = ? OR creditTransactionId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, accountId);
            preparedStatement.setInt(2, accountId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int debitTransactionId = resultSet.getInt("debitTransactionId");
                    int creditTransactionId = resultSet.getInt("creditTransactionId");
                    double transferAmount = resultSet.getDouble("transferAmount");
                    Timestamp transferDate = Timestamp.valueOf(resultSet.getTimestamp("transferDate").toLocalDateTime());

                    TransferHistoryEntry entry = new TransferHistoryEntry(debitTransactionId, creditTransactionId, transferAmount, transferDate);
                    transferHistories.add(entry);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return transferHistories;
    }

    public void transferMoney(int debitAccountId, int creditAccountId, double amount, Timestamp transferDate) {
        try {
            // Récupérer le taux de change à la date du transfert pour la devise de débit (euros)
            int euroCurrencyId = 1;
            double exchangeRate = getExchangeRate(transferDate.toLocalDateTime(), euroCurrencyId);

            // Convertir le montant du transfert en euros en Ariary
            double amountInAriary = amount * exchangeRate;

            // Mettre à jour le solde du compte débiteur (en euros)
            updateAccountBalance(debitAccountId, -amount);

            // Mettre à jour le solde du compte créditeur (en Ariary)
            updateAccountBalance(creditAccountId, amountInAriary);

            // Enregistrez les détails du transfert dans la table TransferHistory
            saveTransferHistory(debitAccountId, creditAccountId, amountInAriary, transferDate.toLocalDateTime());

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du transfert d'argent", e);
        }
    }

    private void updateAccountBalance(int accountId, double amount) throws SQLException {
        String sql = "UPDATE Account SET balance = balance + ? WHERE accountId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDouble(1, amount);
            preparedStatement.setInt(2, accountId);
            preparedStatement.executeUpdate();
        }
    }

    private void saveTransferHistory(int debitAccountId, int creditAccountId, double amount, LocalDateTime transferDate) throws SQLException {
        String sql = "INSERT INTO TransferHistoryEntry (debit_account_id, credit_account_id, transfer_amount, transfer_date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, debitAccountId);
            preparedStatement.setInt(2, creditAccountId);
            preparedStatement.setDouble(3, amount);
            preparedStatement.setObject(4, transferDate);
            preparedStatement.executeUpdate();
        }
    }

    private double getExchangeRate(LocalDateTime date, int currencyId) {
        String sql = "SELECT value FROM CurrencyValue WHERE id_devise_source = ? AND date_effet <= ? ORDER BY date_effet DESC LIMIT 1";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, currencyId);
            preparedStatement.setObject(2, date);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    double exchangeRate = resultSet.getDouble("value");
                    return exchangeRate != 0.0 ? exchangeRate : 1.0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 1.0;
    }

}
