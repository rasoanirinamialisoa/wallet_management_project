package org.example.model;

import lombok.*;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class BalanceSumsResult {
    private double totalIncome;
    private double totalExpense;


    public void setCategorySums(Map<String, Double> categorySums) {
    }
}

