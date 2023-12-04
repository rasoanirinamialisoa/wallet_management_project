package org.example.model;
import java.util.List;

import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Account {
    private int id;
    private String name;
    private double balance;
    private int currencyId;
    private String accountType;

    private List<Transaction> transactions;
    private List<Currency> currencies;

}

