package data;

import models.Book;
import models.Publisher;
import utils.ConnectionPool;
import utils.dataValidation.DataValidationException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leonti on 2016-03-04.
 */
public class PublisherDB {
    public static List<Publisher> selectAllPublishers()
            throws SQLException, DataValidationException {
        ConnectionPool connectionPool = ConnectionPool.getInstance();

        String query = "SELECT * FROM publisher";

        List<Publisher> publishers = new ArrayList<>();


        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Publisher publisher = initializePublisher(resultSet);
                publishers.add(publisher);
            }
        }

        return publishers;
    }

    public static Publisher selectPublisher(String name)
            throws SQLException, DataValidationException {
        ConnectionPool connectionPool = ConnectionPool.getInstance();

        String query = "SELECT * FROM publisher WHERE name = ?";

        Publisher publisher = null;

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, name);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        publisher = initializePublisher(resultSet);
                    }
                }
            }
        }

        return publisher;
    }

    public static long selectPublisherId(Publisher publisher)
            throws SQLException {
        ConnectionPool connectionPool = ConnectionPool.getInstance();

        String query = "SELECT pub_id FROM publisher WHERE name = ?";

        long id = -1;

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, publisher.getName());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                id = resultSet.getLong("PUB_ID");
            }
        }

        return id;
    }

    public static int insertPublisher(Publisher publisher)
            throws SQLException {
        ConnectionPool connectionPool = ConnectionPool.getInstance();

        String query = "INSERT INTO publisher (name, country, state, city, street_num, street_name) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        int rowAffected;

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, publisher.getName());
            preparedStatement.setString(2, publisher.getCountry());
            preparedStatement.setString(3, publisher.getState());
            preparedStatement.setString(4, publisher.getCity());
            preparedStatement.setInt(5, publisher.getStreetNumber());
            preparedStatement.setString(6, publisher.getStreetName());
            rowAffected = preparedStatement.executeUpdate();
        }

        return rowAffected;
    }

    public static Publisher selectBookPublisher(long bookId)
            throws SQLException, DataValidationException {
        ConnectionPool connectionPool = ConnectionPool.getInstance();

        String query = "SELECT * " +
                "FROM publisher, book_publisher\n" +
                "WHERE publisher.pub_id = book_publisher.pub_id\n" +
                "AND book_publisher.book_id = ?";

        Publisher publisher = null;

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setLong(1, bookId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        publisher = initializePublisher(resultSet);
                    }
                }
            }
        }

        return publisher;
    }

    public static int insertBookPublisher(Connection connection, long bookId, Book book)
            throws SQLException {
        String query = "INSERT INTO book_publisher (pub_id, book_id) " +
                "VALUES (?, ?)";

        int rowAffected;

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, selectPublisherId(book.getPublisher()));
            preparedStatement.setLong(2, bookId);
            rowAffected = preparedStatement.executeUpdate();
        }

        return rowAffected;
    }

    public static int deleteBookPublisher(Connection connection, long bookId)
            throws SQLException {
        String query = "DELETE FROM book_publisher WHERE book_id = ?";

        int rowAffected;

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, bookId);
            rowAffected = preparedStatement.executeUpdate();
        }

        return rowAffected;
    }

    private static Publisher initializePublisher(ResultSet resultSet)
            throws SQLException, DataValidationException {
            Publisher publisher =
                    new Publisher(resultSet.getString("NAME"), resultSet.getString("COUNTRY"),
                            resultSet.getString("CITY"), resultSet.getString("STATE"),
                            resultSet.getInt("STREET_NUM"), resultSet.getString("STREET_NAME"));
            return publisher;
    }

//    public static boolean publisherExists(String publisherName)
//            throws SQLException {
//        return selectPublisher(publisherName) != null;
//    }
}
