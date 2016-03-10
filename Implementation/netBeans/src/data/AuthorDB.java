package data;

import models.Author;
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
}
