package org.example.model;
import lombok.*;

import java.sql.Date;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Transaction {
    private int transactionId;
    private int accountId;
    private String description;
    private double amount;
    private Timestamp date;
    private String type;


    public void setDate(Timestamp timestamp) {
        this.date = timestamp;
    }

}

