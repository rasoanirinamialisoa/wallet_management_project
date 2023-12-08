package org.example.model;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class Transaction {
    private int transactionId;
    public static final String TRANSACTION_ID_COLUMN = "transactionId";

    private String labelTransaction;
    public static final String LABEL_COLUMN = "labelTransaction";

    private double amount;
    public static final String AMOUNT_COLUMN = "amount";

    private LocalDateTime dateOfTransaction;
    public static final String DATE_COLUMN = "dateOfTransaction";

    private Account accountId;
    public static final String ACCOUNT_COLUMN = "accountId";

    // Constructeur par défaut
    public Transaction() {
    }

    // Constructeur avec tous les champs
    public Transaction(int transactionId, String labelTransaction, double amount, LocalDateTime dateOfTransaction, Account accountId) {
        this.transactionId = transactionId;
        this.labelTransaction = labelTransaction;
        this.amount = amount;
        this.dateOfTransaction = dateOfTransaction;
        this.accountId = accountId;
    }

    // Getters et Setters pour tous les champs
    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public String getLabelTransaction() {
        return labelTransaction;
    }

    public void setLabelTransaction(String labelTransaction) {
        this.labelTransaction = labelTransaction;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDateTime getDateOfTransaction() {
        return dateOfTransaction;
    }

    public void setDateOfTransaction(LocalDateTime dateOfTransaction) {
        this.dateOfTransaction = dateOfTransaction;
    }

    public Account getAccountId() {
        return accountId;
    }

    public void setAccountId(Account accountId) {
        this.accountId = accountId;
    }

    public void setAccount(Currency currencyId) {
    }
}
