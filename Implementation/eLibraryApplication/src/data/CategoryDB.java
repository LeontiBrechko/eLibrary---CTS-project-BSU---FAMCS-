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
    public static Category selectCategoty(String categoryName)
            throws SQLException {
        ConnectionPool connectionPool = ConnectionPool.getInstance();

        String query = "SELECT * FROM category WHERE name = ?";

        Category category = null;

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, categoryName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                category = new Category();
                category.setName(resultSet.getString("NAME"));
                category.setDescription(resultSet.getString("DESC"));
            }
        }

        return category;
    }

    public static List<Category> selectAllCategories()
            throws SQLException {
        ConnectionPool connectionPool = ConnectionPool.getInstance();

        String query = "SELECT * FROM category";

        List<Category> categories = new ArrayList<>();

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Category category = new Category();
                category.setName(resultSet.getString("NAME"));
                category.setDescription(resultSet.getString("DESC"));
                categories.add(category);
            }
        }

        return categories;
    }

    public static List<Category> selectBookCategories(long bookId)
            throws SQLException {
        ConnectionPool connectionPool = ConnectionPool.getInstance();

        String query = "SELECT category.name, category.desc\n" +
                "FROM category, book_category\n" +
                "WHERE category.category_id = book_category.category_id \n" +
                "AND book_category.book_id = ?";

        List<Category> categories = new ArrayList<>();

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, bookId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Category category = new Category();
                category.setName(resultSet.getString("NAME"));
                category.setDescription(resultSet.getString("DESC"));
                categories.add(category);
            }
        }

        return categories;
    }

    public static int insertBookCategories (Connection connection, long bookId, Book book)
            throws SQLException {
        String query = "INSERT INTO book_category (book_id, category_id) " +
                "VALUES (?, ?)";

        int rowAffected = 0;

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            for (Category category : book.getCategories()) {
                preparedStatement.setLong(1, bookId);
                preparedStatement.setLong(2, selectCategoryId(category));
                preparedStatement.addBatch();
            }

            int[] temp = preparedStatement.executeBatch();
            for (int i : temp) {
                rowAffected += i;
            }
        }

        return rowAffected;
    }

    public static int deleteBookCategories(Connection connection, long bookId)
            throws SQLException {
        String query = "DELETE FROM book_category WHERE book_id = ?";

        int rowAffected;

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, bookId);
            rowAffected = preparedStatement.executeUpdate();
        }

        return rowAffected;
    }

    public static long selectCategoryId(Category category) throws SQLException {
        ConnectionPool connectionPool = ConnectionPool.getInstance();

        String query = "SELECT category.category_id FROM category WHERE category.name = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, category.getName());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getLong(1);
            } else {
                return -1;
            }
        }
    }
}
