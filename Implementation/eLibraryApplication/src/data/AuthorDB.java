package data;

import models.Author;
import models.Book;
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
public class AuthorDB {
    public static List<Author> selectAllAuthors()
            throws SQLException {
        ConnectionPool connectionPool = ConnectionPool.getInstance();

        String query = "SELECT * FROM author";

        List<Author> authors = new ArrayList<>();

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Author author = new Author();
                author.setFirstName(resultSet.getString("FIRST_NAME"));
                author.setLastName(resultSet.getString("LAST_NAME"));
                authors.add(author);
            }
        }

        return authors;
    }

    public static int insertAuthor(Author author)
            throws SQLException {
        ConnectionPool connectionPool = ConnectionPool.getInstance();

        String query = "INSERT INTO author (first_name, last_name) " +
                "VALUES (?, ?)";

        int rowAffected;

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, author.getFirstName());
            preparedStatement.setString(2, author.getLastName());
            rowAffected = preparedStatement.executeUpdate();
        }

        return rowAffected;
    }

    public static long selectAuthorId(Author author)
            throws SQLException {
        ConnectionPool connectionPool = ConnectionPool.getInstance();

        String query = "SELECT auth_id FROM author WHERE first_name = ? AND last_name = ?";

        long id = -1;

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, author.getFirstName());
            preparedStatement.setString(2, author.getLastName());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                id = resultSet.getLong("auth_id");
            }
        }

        return id;
    }

    public static List<Author> selectBookAuthors(long bookId) throws SQLException {
        ConnectionPool connectionPool = ConnectionPool.getInstance();

        String query = "SELECT author.first_name, author.last_name\n" +
                "FROM author, book_author\n" +
                "WHERE author.auth_id = book_author.auth_id\n" +
                "AND book_author.book_id = ?";

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setLong(1, bookId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    List<Author> authors = new ArrayList<>();
                    while (resultSet.next()) {
                        Author author = new Author();
                        author.setFirstName(resultSet.getString(1));
                        author.setLastName(resultSet.getString(2));
                        authors.add(author);
                    }
                    if (authors.size() == 0) {
                        return null;
                    } else {
                        return authors;
                    }
                }
            }
        }
    }

    public static int insertBookAuthors (Connection connection, long bookId, Book book)
            throws SQLException{
        String query = "INSERT INTO book_author (book_id, auth_id) " +
                "VALUES (?, ?)";

        int rowAffected = 0;

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            for (Author author : book.getAuthors()) {
                preparedStatement.setLong(1, bookId);
                preparedStatement.setLong(2, selectAuthorId(author));
                preparedStatement.addBatch();
            }
            int[] temp = preparedStatement.executeBatch();
            for (int i : temp) {
                rowAffected += i;
            }
        }

        return rowAffected;
    }

    public static int deleteBookAuthors(Connection connection, long bookId)
            throws SQLException{
        String query = "DELETE FROM book_author WHERE book_id = ?";

        int rowAffected;

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, bookId);
            rowAffected = preparedStatement.executeUpdate();
        }

        return rowAffected;
    }
}
