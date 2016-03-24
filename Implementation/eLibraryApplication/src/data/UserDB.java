package data;

import models.User;
import utils.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Leonti on 2016-03-05.
 */
public class UserDB {
    public static void insertUser(User user)
            throws SQLException {
        long accountId = AccountDB.insertAccount(user.getAccount());

        ConnectionPool connectionPool = ConnectionPool.getInstance();

        String query = "INSERT INTO user (first_name, last_name, account_id) " +
                "VALUES (?, ?, ?)";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setLong(3, accountId);
            preparedStatement.executeUpdate();
        }
    }
}
