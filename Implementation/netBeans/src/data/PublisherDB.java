package data;

import models.Book;
import models.Publisher;
import utils.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Leonti on 2016-03-04.
 */
public class PublisherDB {
    public static Publisher selectBookPublisher(long bookId) throws SQLException {
        ConnectionPool connectionPool = ConnectionPool.getInstance();

        String query = "SELECT publisher.name, publisher.address_id\n" +
                "FROM publisher, book_publisher\n" +
                "WHERE publisher.pub_id = book_publisher.pub_id\n" +
                "AND book_publisher.book_id = ?";

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setLong(1, bookId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        Publisher publisher = new Publisher();
                        publisher.setName(resultSet.getString(1));
                        publisher.setAddress(AddressDB.selectPublisherAddress(resultSet.getLong(2)));
                        return publisher;
                    } else {
                        return null;
                    }
                }
            }
        }
    }
}
