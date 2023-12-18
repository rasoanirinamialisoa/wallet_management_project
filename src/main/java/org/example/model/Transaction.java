package org.example.model;
import lombok.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Transaction {
    private int transactionId;
    private int accountId;
    private String labelTransaction;
    private double amount;
    private Timestamp dateOfTransaction;
    private String transactionsType;
    private int categoryId;

    public static final String TRANSACTION_ID = "transactionId";
    public static final String ACCOUNT_ID = "accountId";
    public static final String LABEL_TRANSACTION = "labelTransaction";
    public static final String AMOUNT = "amount";
    public static final String DATE_OF_TRANSACTION = "dateOfTransaction";
    public static final String TRANSACTIONSTYPE = "transactionsType";
    public static final String CATEGORY_ID = "categoryId";
}

