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
    private Category category;
}

