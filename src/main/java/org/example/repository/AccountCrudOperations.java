package org.example.repository;

import org.example.model.Account;
import org.example.model.Currency;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.example.model.Account.*;

public class AccountCrudOperations implements CrudOperations<Account> {
    private final Connection connection;
    public AccountCrudOperations(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Account> findAll() {
        List<Account> accounts = new ArrayList<>();
        try {
            String query = "SELECT * FROM account";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Account account = mapResultSetToAccount(resultSet);
                    accounts.add(account);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accounts;
    }

    @Override
    public List<Account> saveAll(List<Account> toSave) {
        String query = "INSERT INTO account (name, balance, currencyId, accountType) VALUES (?, ?, ?, ?)";
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                for (Account account : toSave) {
                    preparedStatement.setString(1, account.getName());
                    preparedStatement.setDouble(2, account.getBalance());
                    preparedStatement.setInt(3, account.getCurrencyId().getCurrencyId());
                    preparedStatement.setString(4, account.getAccountType());
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
    public Account save(Account toSave) {
        String query = "INSERT INTO account (name, balance, currencyId, accountType) VALUES (?, ?, ?, ?) RETURNING *";
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, toSave.getName());
                preparedStatement.setDouble(2, toSave.getBalance());
                preparedStatement.setInt(3, toSave.getCurrencyId().getCurrencyId());
                preparedStatement.setString(4, toSave.getAccountType());

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return mapResultSetToAccount(resultSet);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Currency findById(int accountId) {
        String query = "SELECT * FROM account WHERE " + ACCOUNT_ID_COLUMN + " = ?";
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, accountId);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return mapResultSetToAccount(resultSet).getCurrencyId();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Retourner null si aucun compte n'est trouvé avec l'ID spécifié
    }

    private Account mapResultSetToAccount(ResultSet resultSet) throws SQLException {
        Account account = new Account();
        account.setAccountId(resultSet.getInt(ACCOUNT_ID_COLUMN));
        account.setName(resultSet.getString(NAME_COLUMN));
        account.setBalance(resultSet.getDouble(BALANCE_COLUMN));

        Currency currency = (Currency) resultSet.getObject(CURRENCY_ID_COLUMN);
        account.setCurrencyId(currency);

        account.setAccountType(resultSet.getString(ACCOUNT_TYPE_COLUMN));
        return account;
    }


}

