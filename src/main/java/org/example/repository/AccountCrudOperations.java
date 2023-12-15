package org.example.repository;

import org.example.model.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
                    preparedStatement.setInt(3, account.getCurrencyId());
                    preparedStatement.setString(4, account.getAccountType());
                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // tsy de ilaina le  e.printStackTrace(); ity, aleo aza atao throw ftsn. na try exception ftsn.
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
                preparedStatement.setInt(3, toSave.getCurrencyId());
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


    public void saveBalance(int accountId, double balance) {
        String query = "UPDATE account SET balance = ? WHERE accountid = ?";
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setDouble(1, balance);
                preparedStatement.setInt(2, accountId);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Account mapResultSetToAccount(ResultSet resultSet) throws SQLException {
        Account account = new Account();
        account.setAccountid(resultSet.getInt(Account.ACCOUNT_ID));
        account.setName(resultSet.getString(Account.NAME));
        account.setBalance(resultSet.getDouble(Account.BALANCE));
        account.setCurrencyId(resultSet.getInt(Account.CURRENCY_ID));
        account.setAccountType(resultSet.getString(Account.ACCOUNT_TYPE));
        return account;
    }
}

