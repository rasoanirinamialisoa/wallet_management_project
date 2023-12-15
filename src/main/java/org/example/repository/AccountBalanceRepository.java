package org.example.repository;

import org.example.model.Account;
import org.example.model.TransferHistoryEntry;

import java.math.BigDecimal;
import java.sql.*;
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
            e.printStackTrace(); // Ajout d'une trace pour la gestion des erreurs
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                // Gérer l'exception
                e.printStackTrace(); // Ajout d'une trace pour la gestion des erreurs
            }
        }

        return currentBalance;
    }

    public List<TransferHistoryEntry> findAll(int accountId) {
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
                    Timestamp transferDate = resultSet.getTimestamp("transferDate");

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
            save(debitAccountId, -amount);

            // Mettre à jour le solde du compte créditeur (en Ariary)
            save(creditAccountId, amountInAriary);

            // Enregistrez les détails du transfert dans la table TransferHistory
            saveTransferHistory(debitAccountId, creditAccountId, amountInAriary, transferDate.toLocalDateTime());

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du transfert d'argent", e);
        }
    }


    public Account save(int accountId, double balance) throws SQLException {
        String query = "UPDATE Account SET balance = balance + ? WHERE accountId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setDouble(1, balance);
            preparedStatement.setInt(2, accountId);
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                // Obtenez l'ID généré si nécessaire
                int generatedAccountId = generatedKeys.getInt(1);

                // Construisez et retournez l'objet Account mis à jour
                Account updatedAccount = new Account();
                updatedAccount.setAccountId(generatedAccountId);
                updatedAccount.setBalance(balance);
                return updatedAccount;
            } else {
                // Si vous n'avez pas besoin de l'ID généré, retournez simplement un nouvel objet Account avec les valeurs mises à jour
                Account updatedAccount = new Account();
                updatedAccount.setAccountId(accountId);
                updatedAccount.setBalance(balance);
                return updatedAccount;
            }
        }
    }


    public void saveTransferHistory(int debitTransactionId, int creditTransactionId, double transferAmount, LocalDateTime transferDate) throws SQLException {
        String sql = "INSERT INTO TransferHistoryEntry (debitTransactionId, creditTransactionId, transferAmount, transferDate) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, debitTransactionId);
            preparedStatement.setInt(2, creditTransactionId);
            preparedStatement.setDouble(3, transferAmount);
            preparedStatement.setObject(4, transferDate);
            preparedStatement.executeUpdate();
        }
    }

    public double getExchangeRate(LocalDateTime date, int currencyId) {
        String sql = "SELECT montant FROM CurrencyValue WHERE id_devise_source = ? AND date_effet <= ? ORDER BY date_effet DESC LIMIT 1";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, currencyId);
            preparedStatement.setObject(2, Timestamp.valueOf(date));

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    double exchangeRate = resultSet.getDouble("montant");
                    return exchangeRate != 0.0 ? exchangeRate : 1.0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 1.0;
    }


}
