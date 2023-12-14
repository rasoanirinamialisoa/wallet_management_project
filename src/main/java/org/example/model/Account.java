package org.example.model;
import java.util.List;

import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Account {
    private int accountId;
    private String name;
    private double balance;
    private int currencyId;
    private String accountType;
    private List<Transaction> transactions;
    private List<Currency> currencies;

    public static final String ACCOUNT_ID = "accountid";
    public static final String NAME = "name";
    public static final String BALANCE = "balance";
    public static final String CURRENCY_ID = "currencyId";
    public static final String ACCOUNT_TYPE = "accountType";
    public void setAccountid(int accountid) {
    }
}

