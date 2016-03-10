package data;

import models.Account;
import models.enums.AccountRole;
import models.enums.AccountState;
import utils.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Leonti on 2016-02-29.
 */
public class AccountDB {
    // TODO: 2016-02-29 accountDB insertAccount method
    public static void insertAccount(Account account) {

    }

    // TODO: 2016-02-29 implement accountDB selectAccount method
    public static Account selectAccount(String emailOrUsername) throws SQLException {
        ConnectionPool connectionPool = ConnectionPool.getInstance();

        boolean selectByEmail = emailOrUsername.contains("@");

        String query = "SELECT * FROM account WHERE " +
                (selectByEmail ? "email" : "username") +
                " = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, emailOrUsername);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Account account = new Account();
                account.setUsername(resultSet.getString("USERNAME"));
                account.setEmail(resultSet.getString("EMAIL"));
                account.setPassword(resultSet.getString("PASSWORD"));
                account.setSaltValue(resultSet.getString("SALT_VALUE"));
                account.setRole(AccountRole.valueOf(resultSet.getString("ROLE")));
                account.setCreationTime(resultSet.getTimestamp("CREATION_TIME").toLocalDateTime());
                account.setState(AccountState.valueOf(resultSet.getString("STATE")));
                return account;
            } else {
                return null;
            }
        }
    }

    // TODO: 2016-02-29 implement accountDB update method
    public static void updateAccount(Account account) {

    }

    // TODO: 2016-02-29 accountDB emailExists method
    public static boolean emailExists(String email) {
        return false;
    }

    // TODO: 2016-02-29 accountDB usernameExists method
    public static boolean usernameExists(String login) {
        return false;
    }


}
