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

        try {
            connection.setAutoCommit(false);

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

            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackException) {
                rollbackException.printStackTrace(); // ou loguer l'erreur
            }
            throw new RuntimeException("Erreur lors du calcul du solde", e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace(); // ou loguer l'erreur
            }
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


    public void transferMoney(int debitAccountId, int creditAccountId, BigDecimal amount, LocalDateTime transferDate) {
        try {
            // Récupérer le taux de change à la date du transfert pour la devise de débit (euros)
            int euroCurrencyId = 1;
            BigDecimal exchangeRate = getExchangeRate(transferDate, euroCurrencyId); 

            // Convertir le montant du transfert en euros en Ariary
            BigDecimal amountInAriary = amount.multiply(exchangeRate);

            // Mettre à jour le solde du compte débiteur (en euros)
            updateAccountBalance(debitAccountId, amount.negate());

            // Mettre à jour le solde du compte créditeur (en Ariary)
            updateAccountBalance(creditAccountId, amountInAriary);

            // Enregistrez les détails du transfert dans la table TransferHistory
            saveTransferHistory(debitAccountId, creditAccountId, amountInAriary, transferDate);

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du transfert d'argent", e);
        }
    }

    private void updateAccountBalance(int accountId, BigDecimal amount) throws SQLException {
        String sql = "UPDATE Account SET balance = balance + ? WHERE accountId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setBigDecimal(1, amount);
            preparedStatement.setInt(2, accountId);
            preparedStatement.executeUpdate();
        }
    }

    private void saveTransferHistory(int debitAccountId, int creditAccountId, BigDecimal amount, LocalDateTime transferDate) throws SQLException {
        String sql = "INSERT INTO TransferHistory (debit_account_id, credit_account_id, transfer_amount, transfer_date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, debitAccountId);
            preparedStatement.setInt(2, creditAccountId);
            preparedStatement.setBigDecimal(3, amount);
            preparedStatement.setObject(4, transferDate);
            preparedStatement.executeUpdate();
        }
    }

    private BigDecimal getExchangeRate(LocalDateTime date, int currencyId) {
        String sql = "SELECT value FROM CurrencyValue WHERE id_devise_source = ? AND date_effet <= ? ORDER BY date_effet DESC LIMIT 1";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, currencyId);
            preparedStatement.setObject(2, date);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    BigDecimal exchangeRate = resultSet.getBigDecimal("value");
                    return exchangeRate != null ? exchangeRate : BigDecimal.ONE;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return BigDecimal.ONE;
    }
}
