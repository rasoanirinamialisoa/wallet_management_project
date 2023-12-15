package org.example.model;


import lombok.*;

import java.sql.Timestamp;


@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyValue {
    private int CurrencyValueId;
    private int ID_Devise_source;
    private int ID_Devise_destination;
    private Double Montant;
    private Timestamp Date_effet;

}