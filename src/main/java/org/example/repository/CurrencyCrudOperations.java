package org.example.repository;

import org.example.model.Currency;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CurrencyCrudOperations implements CrudOperations<Currency> {

    private Connection connection;

    public CurrencyCrudOperations(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Currency> findAll() {
        List<Currency> currencies = new ArrayList<>();
        try {
            String query = "SELECT * FROM currency";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Currency currency = mapResultSetToCurrency(resultSet);
                    currencies.add(currency);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return currencies;
    }

    @Override
    public List<Currency> saveAll(List<Currency> toSave) {
        String query = "INSERT INTO currency (currencyCode, currencyName) VALUES (?, ?)";
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                for (Currency currency : toSave) {
                    preparedStatement.setString(1, currency.getCurrencyCode());
                    preparedStatement.setString(2, currency.getCurrencyName());
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
    public Currency save(Currency toSave) {
        String query = "INSERT INTO currency (currencyCode, currencyName) VALUES (?, ?) RETURNING *";
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, toSave.getCurrencyCode());
                preparedStatement.setString(2, toSave.getCurrencyName());

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return mapResultSetToCurrency(resultSet);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }



    private Currency mapResultSetToCurrency(ResultSet resultSet) throws SQLException {
        Currency currency = new Currency();
        currency.setCurrencyId(resultSet.getInt(Currency.CURRENCY_ID));
        currency.setCurrencyCode(resultSet.getString(Currency.CURRENCY_CODE));
        currency.setCurrencyName(resultSet.getString(Currency.CURRENCY_NAME));
        return currency;
    }
}

