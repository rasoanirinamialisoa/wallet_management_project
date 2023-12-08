package org.example.model;

import lombok.*;

@Getter
@Setter
@ToString
public class Account {
    private int accountId;
    public static final String ACCOUNT_ID_COLUMN = "accountId";

    private String name;
    public static final String NAME_COLUMN = "name";

    private double balance;
    public static final String BALANCE_COLUMN = "balance";

    private String accountType;
    public static final String ACCOUNT_TYPE_COLUMN = "accountType";  // Correction ici

    private Currency currencyId;
    public static final String CURRENCY_ID_COLUMN = "currencyId";

    // Constructeur par défaut
    public Account() {
    }
    public Account(int accountId, String name, double balance, String accountType, Currency currencyId) {
        this.accountId = accountId;
        this.name = name;
        this.balance = balance;
        this.accountType = accountType;
        this.currencyId = currencyId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public Currency getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Currency currencyId) {
        this.currencyId = currencyId;
    }
}
