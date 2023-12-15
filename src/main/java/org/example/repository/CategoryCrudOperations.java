package org.example.repository;
import org.example.model.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryCrudOperations implements CrudOperations<Category> {
    private final Connection connection;


    public CategoryCrudOperations(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Category> findAll() {
        List<Category> categories = new ArrayList<>();
        try {
            String query = "SELECT * FROM categories";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Category category = mapResultSetToCategory(resultSet);
                    categories.add(category);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }



    @Override
    public List<Category> saveAll(List<Category> toSave) {
        String query = "INSERT INTO categories (categoryName) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM categories WHERE categoryName = ?)";
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                for (Category category : toSave) {
                    preparedStatement.setString(1, category.getCategoryName());
                    preparedStatement.setString(2, category.getCategoryName());
                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toSave;
    }

    @Override
    public Category save(Category toSave) {
        String query = "INSERT INTO categories (categoryName) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM categories WHERE categoryName = ?) RETURNING *";
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, toSave.getCategoryName());
                preparedStatement.setString(2, toSave.getCategoryName());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return mapResultSetToCategory(resultSet);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static Category mapResultSetToCategory(ResultSet resultSet) throws SQLException {
        Category category = new Category();
        category.setCategoryId(resultSet.getInt(Category.CATEGORY_ID));
        category.setCategoryName(resultSet.getString(Category.CATEGORIE_NAME));

        return category;
    }
}

