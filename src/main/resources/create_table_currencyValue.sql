CREATE TABLE CurrencyValue (
                               CurrencyValueId SERIAL PRIMARY KEY,
                               ID_Devise_source INT,
                               ID_Devise_destination INT,
                               Montant DOUBLE PRECISION,
                               Date_effet TIMESTAMP
);
INSERT INTO CurrencyValue (ID_Devise_source, ID_Devise_destination, Montant, Date_effet) VALUES (1, 2, 4500.00, '2023-12-05');
INSERT INTO CurrencyValue (ID_Devise_source, ID_Devise_destination, Montant, Date_effet) VALUES (1, 2, 4600.00, '2023-12-06');
INSERT INTO CurrencyValue (ID_Devise_source, ID_Devise_destination, Montant, Date_effet) VALUES (1, 2, 4700.00, '2023-12-07');


