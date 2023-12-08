package org.example.repository;

import org.example.model.Currency;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.example.model.Currency.*;

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

    @Override
    public Currency findById(int currencyId) {
        String query = "SELECT * FROM currency WHERE " + ID_COLUMN_CURRENCY + " = ?";
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, currencyId);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return mapResultSetToCurrency(resultSet);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Retourner null si aucune devise n'est trouvée avec l'ID spécifié
    }
    private Currency mapResultSetToCurrency(ResultSet resultSet) throws SQLException {
        Currency currency = new Currency();
        currency.setCurrencyId(resultSet.getInt(ID_COLUMN_CURRENCY));
        currency.setCurrencyCode(resultSet.getString(CURRENCY_CODE_COLUMN));
        currency.setCurrencyName(resultSet.getString(CURRENCY_NAME_COLUMN));
        return currency;
    }
}

