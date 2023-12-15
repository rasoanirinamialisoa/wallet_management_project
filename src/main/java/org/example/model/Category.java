package org.example.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class Category {
    private int categoryId;
    private String categoryName;

    public static final String CATEGORY_ID = "categoryId";
    public static final String CATEGORIE_NAME = "categoryName";
}
