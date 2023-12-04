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
}

