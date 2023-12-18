package org.example.repository;
import org.example.model.BalanceSumsResult;
import org.example.model.Category;
import org.example.model.CategorySumsResult;

import java.sql.*;
import java.time.LocalDateTime;
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

    public CategorySumsResult getCategorySumsBetweenDates(int accountId, LocalDateTime startDate, LocalDateTime endDate) {
        CallableStatement callableStatement = null;

        try {
            String sql = "{ ? = call getCategorySumsBetweenDates(?, ?, ?) }";
            callableStatement = connection.prepareCall(sql);

            // Register the return type (OUT) for the first variable
            callableStatement.registerOutParameter(1, Types.OTHER);

            // Input parameters
            callableStatement.setInt(2, accountId);
            callableStatement.setTimestamp(3, Timestamp.valueOf(startDate));
            callableStatement.setTimestamp(4, Timestamp.valueOf(endDate));

            // Execute the stored procedure
            callableStatement.execute();

            // Retrieve the result of the stored procedure
            Object result = callableStatement.getObject(1);

            CategorySumsResult categorySumsResult = new CategorySumsResult();

            if (result instanceof ResultSet) {
                ResultSet resultSet = (ResultSet) result;
                while (resultSet.next()) {
                    // Assuming the columns are named 'restaurant' and 'salaire'
                    double restaurantSum = resultSet.getDouble("restaurant");
                    double salaireSum = resultSet.getDouble("salaire");

                    // Set the values in the CategorySumsResult object
                    categorySumsResult.setRestaurantSum(restaurantSum);
                    categorySumsResult.setSalaireSum(salaireSum);
                }
            }

            return categorySumsResult;
        } catch (SQLException e) {
            // Handle the exception
            e.printStackTrace();
            throw new RuntimeException("Error executing stored procedure", e);
        } finally {
            // Close resources
            try {
                if (callableStatement != null) {
                    callableStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // Handle the exception
                e.printStackTrace();
            }
        }
    }


    private static Category mapResultSetToCategory(ResultSet resultSet) throws SQLException {
        Category category = new Category();
        category.setCategoryId(resultSet.getInt(Category.CATEGORY_ID));
        category.setCategoryName(resultSet.getString(Category.CATEGORIE_NAME));

        return category;
    }
}

