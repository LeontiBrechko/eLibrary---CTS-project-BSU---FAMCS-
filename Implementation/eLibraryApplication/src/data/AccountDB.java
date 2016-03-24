package data;

import models.Account;
import models.Book;
import models.enums.AccountRole;
import models.enums.AccountState;
import utils.ConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leonti on 2016-02-29.
 */
public class AccountDB {
    public static List<Account> selectAccountList()
            throws SQLException {
        ConnectionPool connectionPool = ConnectionPool.getInstance();

        String query = "SELECT * FROM account";

        List<Account> accounts = new ArrayList<>();

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            addAccountToList(connection, resultSet, accounts);
        }

        return accounts;
    }

    public static Account selectAccount(String emailOrUsername)
            throws SQLException {
        ConnectionPool connectionPool = ConnectionPool.getInstance();

        boolean selectByEmail = emailOrUsername.contains("@");

        String query = "SELECT * FROM account WHERE " +
                (selectByEmail ? "email" : "username") +
                " = ?";

        List<Account> accounts = new ArrayList<>(1);
        Account account = null;

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, emailOrUsername.toLowerCase());
            ResultSet resultSet = preparedStatement.executeQuery();
            addAccountToList(connection, resultSet, accounts);
            if (accounts.size() > 0) {
                account = accounts.get(0);
            }
        }

        return account;
    }

    public static long insertAccount(Account account)
            throws SQLException {
        ConnectionPool connectionPool = ConnectionPool.getInstance();

        String query =
                "INSERT INTO account (username, email, password, " +
                        "salt_value, role, creation_time, state, confirm_token) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        long id;

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, account.getUsername());
                preparedStatement.setString(2, account.getEmail());
                preparedStatement.setString(3, account.getPassword());
                preparedStatement.setString(4, account.getSaltValue());
                preparedStatement.setString(5, account.getRole().toString());
                preparedStatement.setTimestamp(6, Timestamp.valueOf(account.getCreationTime()));
                preparedStatement.setString(7, account.getState().toString());
                preparedStatement.setString(8, account.getConfirmationToken());
                preparedStatement.executeUpdate();
            }

            id = selectAccountID(connection, account.getUsername());
            insertAccountDownloadList(connection, id, account.getDownloadList());
        }

        return id;
    }

    public static void updateAccount(Account account)
            throws SQLException {
        ConnectionPool connectionPool = ConnectionPool.getInstance();

        String query = "UPDATE account SET " +
                "email = ?, " +
                "password = ?, " +
                "salt_value = ?, " +
                "role = ?, " +
                "state = ? " +
                "WHERE username = ? ";

        long id;

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, account.getEmail());
            preparedStatement.setString(2, account.getPassword());
            preparedStatement.setString(3, account.getSaltValue());
            preparedStatement.setString(4, account.getRole().toString());
            preparedStatement.setString(5, account.getState().toString());
            preparedStatement.setString(6, account.getUsername());
            preparedStatement.executeUpdate();

            id = selectAccountID(connection, account.getUsername());
            updateAccountDownloadList(connection, id, account.getDownloadList());
        }
    }

    public static boolean emailExists(String email)
            throws SQLException {
        return selectAccount(email) != null;
    }

    public static boolean usernameExists(String username)
            throws SQLException {
        return selectAccount(username) != null;
    }

    private static long selectAccountID(Connection connection, String username)
            throws SQLException {
        String query = "SELECT account_id FROM account WHERE username = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getLong(1);
            } else {
                throw new SQLException("Cannot select account id for username:  " +
                        username);
            }
        }
    }

    private static long[] insertAccountDownloadList(Connection connection, long accountId,
                                                  List<Book> downloadList)
            throws SQLException {
        String query = "INSERT INTO account_download_list (account_id, book_id) " +
                "VALUES (?, ?)";

        List<String> bookIsbn13List = new ArrayList<>(downloadList.size());
        downloadList.stream().forEach((b) -> bookIsbn13List.add(b.getIsbn13()));

        long[] bookIds = BookDB.selectBookIds(connection, bookIsbn13List);

        PreparedStatement preparedStatement = null;
        for (int i = 0; i < downloadList.size(); i++) {
            preparedStatement = connection.prepareStatement(query);
                preparedStatement.setLong(1, accountId);
                preparedStatement.setLong(2, bookIds[i]);
                preparedStatement.executeUpdate();
        }
        if (preparedStatement != null) {
            preparedStatement.close();
        }


        return bookIds;
    }

    private static List<Book> selectAccountDownloadList(Connection connection, long accountId)
            throws SQLException {
        String query = "SELECT book_id " +
                "FROM account_download_list " +
                "WHERE account_id = ?";

        List<Book> downloadList = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, accountId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Book book = BookDB.selectBook(resultSet.getLong(1));
                downloadList.add(book);
            }
        }

        return downloadList;
    }

    private static void updateAccountDownloadList(Connection connection, long accountId,
                                                  List<Book> downloadList)
            throws SQLException {
        deleteAccountDownloadList(connection, accountId);
        insertAccountDownloadList(connection, accountId, downloadList);
    }

    private static void deleteAccountDownloadList(Connection connection, long accountId)
            throws SQLException {
        String query = "DELETE FROM account_download_list WHERE account_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, accountId);
            preparedStatement.executeUpdate();
        }

    }

    private static void addAccountToList(Connection connection,
                                         ResultSet resultSet, List<Account> accounts)
            throws SQLException {
        while (resultSet.next()) {
            Account account = new Account();
            account.setUsername(resultSet.getString("USERNAME"));
            account.setEmail(resultSet.getString("EMAIL"));
            account.setPassword(resultSet.getString("PASSWORD"));
            account.setSaltValue(resultSet.getString("SALT_VALUE"));
            account.setRole(AccountRole.valueOf(resultSet.getString("ROLE")));
            account.setCreationTime(resultSet.getTimestamp("CREATION_TIME").toLocalDateTime());
            account.setState(AccountState.valueOf(resultSet.getString("STATE")));
            account.setConfirmationToken(resultSet.getString("CONFIRM_TOKEN"));

            long id = selectAccountID(connection, account.getUsername());
            account.setDownloadList(selectAccountDownloadList(connection, id));

            accounts.add(account);
        }
    }
}
