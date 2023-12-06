package org.example.model;
import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Currency {
    private int id;
    private String currencyCode;
    private String currencyName;
    // private float exchangeRate;
    // private Date updatedAt;
    // timestamp: dépendente amle JDBC
    // instant : timpestamp without timezone. pas notion de fuseau horaire UTC+0
    //locale date : misy fuseau horaire mintsy
}

