package data;

import models.Book;
import models.Category;
import utils.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leonti on 2016-03-04.
 */
public class CategoryDB {
    public static List<Category> selectBookCategories(long bookId) throws SQLException {
        ConnectionPool connectionPool = ConnectionPool.getInstance();

        String query = "SELECT category.name, category.desc\n" +
                "FROM category, book_category\n" +
                "WHERE category.category_id = book_category.category_id \n" +
                "AND book_category.book_id = ?";

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setLong(1, bookId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    List<Category> categories = new ArrayList<>();
                    while (resultSet.next()) {
                        Category category = new Category();
                        category.setName(resultSet.getString(1));
                        category.setDescription(resultSet.getString(2));
                        categories.add(category);
                    }
                    if (categories.size() == 0) {
                        return null;
                    } else {
                        return categories;
                    }
                }
            }
        }
    }

    public static long selectCategoryId(String categoryName) throws SQLException {
        ConnectionPool connectionPool = ConnectionPool.getInstance();

        String query = "SELECT category.category_id FROM category WHERE category.name = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, categoryName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getLong(1);
            } else {
                return -1;
            }
        }
    }
}
