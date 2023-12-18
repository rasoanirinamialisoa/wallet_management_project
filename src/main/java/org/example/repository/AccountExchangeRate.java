package org.example.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.example.Main.logger;

public class AccountExchangeRate {

    private Connection connection;

    public AccountExchangeRate(Connection connection) {
        this.connection = connection;
    }

    public enum ExchangeRateCalculationType {
        WEIGHTED_AVERAGE,
        MINIMUM,
        MAXIMUM,
        MEDIAN
    }

    public double getExchangeRate(LocalDateTime date, int id_devise_source, ExchangeRateCalculationType calculationType) {
        String sql = "SELECT montant FROM CurrencyValue WHERE id_devise_source = ? AND date_effet::date = ? ORDER BY date_effet";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id_devise_source);
            preparedStatement.setObject(2, date);

            // Log de la requête SQL
            logger.info("Executing SQL query: {}", preparedStatement.toString());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Double> exchangeRates = new ArrayList<>();

                while (resultSet.next()) {
                    double exchangeRate = resultSet.getDouble("value");
                    exchangeRates.add(exchangeRate);
                }

                if (exchangeRates.isEmpty()) {
                    // Log du cas où aucun taux de change n'est trouvé
                    logger.warn("No exchange rates found for the specified date. Returning default value: 1.0");
                    return 1.0;
                }

                // Calcul du taux de change en fonction du type de calcul
                double calculatedExchangeRate;

                switch (calculationType) {
                    case WEIGHTED_AVERAGE:
                        // Calcul de la moyenne pondérée
                        double sum = 0.0;
                        double totalWeight = 0.0;

                        for (int i = 0; i < exchangeRates.size(); i++) {
                            double weight = getWeightForHour(i);
                            sum += exchangeRates.get(i) * weight;
                            totalWeight += weight;
                        }

                        calculatedExchangeRate = sum / totalWeight;
                        break;

                    case MINIMUM:
                        // Récupération de la valeur minimale
                        calculatedExchangeRate = Collections.min(exchangeRates);
                        break;

                    case MAXIMUM:
                        // Récupération de la valeur maximale
                        calculatedExchangeRate = Collections.max(exchangeRates);
                        break;

                    case MEDIAN:
                        // Calcul de la médiane
                        Collections.sort(exchangeRates);
                        int middle = exchangeRates.size() / 2;

                        if (exchangeRates.size() % 2 == 0) {
                            calculatedExchangeRate = (exchangeRates.get(middle - 1) + exchangeRates.get(middle)) / 2.0;
                        } else {
                            calculatedExchangeRate = exchangeRates.get(middle);
                        }
                        break;

                    default:
                        // Par défaut, utiliser la moyenne pondérée
                        sum = 0.0;
                        totalWeight = 0.0;

                        for (int i = 0; i < exchangeRates.size(); i++) {
                            double weight = getWeightForHour(i);
                            sum += exchangeRates.get(i) * weight;
                            totalWeight += weight;
                        }

                        calculatedExchangeRate = sum / totalWeight;
                        break;
                }

                // Log du résultat de la requête
                logger.info("{} exchange rate found: {}", calculationType, calculatedExchangeRate);

                return calculatedExchangeRate;
            }
        } catch (SQLException e) {
            // Log de l'erreur SQL
            logger.error("Error executing SQL query: {}", e.getMessage());

            throw new RuntimeException(e);
        }
    }

    private double getWeightForHour(int hourIndex) {
        // Définir vos poids pour chaque heure de la journée
        double[] weights = {1.0, 1.5, 1.0, 1.2, 1.8, 2.0, 1.5, 1.0, 1.2, 1.5, 2.0, 2.5, 2.0, 1.5, 1.0, 0.8, 0.5, 0.2};

        // Assurez-vous que l'index est dans la plage
        int adjustedIndex = hourIndex % weights.length;

        return weights[adjustedIndex];
    }
}
