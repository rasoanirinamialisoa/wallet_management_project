package org.example.model;

import lombok.*;

@Getter
@Setter
@ToString
public class Currency {
    private int currencyId;
    public static final String ID_COLUMN_CURRENCY = "currencyId";

    private String currencyCode;
    public static final String CURRENCY_CODE_COLUMN = "currencyCode";

    private String currencyName;
    public static final String CURRENCY_NAME_COLUMN = "currencyName";

    // Constructeur par défaut
    public Currency() {
    }

    // Constructeur avec tous les champs
    public Currency(int currencyId, String currencyCode, String currencyName) {
        this.currencyId = currencyId;
        this.currencyCode = currencyCode;
        this.currencyName = currencyName;
    }

    // Getters et Setters pour tous les champs
    public int getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(int currencyId) {
        this.currencyId = currencyId;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }
}
