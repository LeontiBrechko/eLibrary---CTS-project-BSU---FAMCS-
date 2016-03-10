package data;

import models.Address;
import utils.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Leonti on 2016-03-04.
 */
public class AddressDB {
    public static Address selectPublisherAddress(long addressId) throws SQLException {
        ConnectionPool connectionPool = ConnectionPool.getInstance();

        String query = "SELECT country, state, city, street_num, street_name\n" +
                "FROM address\n" +
                "WHERE address.address_id = ?";

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setLong(1, addressId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        Address address = new Address();
                        address.setCountry(resultSet.getString(1));
                        address.setState(resultSet.getString(2));
                        address.setCity(resultSet.getString(3));
                        // TODO: 2016-03-04 check int value if null
                        address.setStreetNumber(resultSet.getInt(4));
                        address.setStreetName(resultSet.getString(5));
                        return address;
                    } else {
                        return null;
                    }
                }
            }
        }
    }
}
