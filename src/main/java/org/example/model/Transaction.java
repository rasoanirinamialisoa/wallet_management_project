package org.example.model;
import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Transaction {
    private int transactionId;
    private String accountId;
    private String description;
    private double amount;
    private String date;
    private String type;

}

