package org.example.model;


import lombok.*;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class CategorySumsResult {
    private double restaurant;
    private double salaire;
    private double restaurantSum;
    private double salaireSum;

    public void setRestaurantSum(double restaurantSum) {
        this.restaurantSum = restaurantSum;
    }

    public void setSalaireSum(double salaireSum) {
        this.salaireSum = salaireSum;
    }

    public void setCategorySums(Map<String, Double> categorySums) {
    }
}

