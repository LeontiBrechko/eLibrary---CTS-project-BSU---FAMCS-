package data;

import models.Book;
import utils.ConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leonti on 2016-03-04.
 */
public class BookDB {
    public static Book selectBook(String isbn13) throws SQLException {
        ConnectionPool connectionPool = ConnectionPool.getInstance();

        String query = "SELECT * FROM book WHERE isbn_13 = ?";
        long bookId = 0;

        Book book = null;

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, isbn13);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                List<Book> temp = new ArrayList<>(1);
                addBookToList(resultSet, temp);
                book = temp.get(0);
            }
        }

        return book;
    }

    public static int updateBook(Book book) throws SQLException {
        ConnectionPool connectionPool = ConnectionPool.getInstance();

        String query = "UPDATE book SET " +
                "book.title = ?, " +
                "book.desc = ?, " +
                "book.popularity = ?, " +
                "book.image = ?, " +
                "book.thumb = ? " +
                "WHERE book.isbn_13 = ?";

        int rowAffected = 0;

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, book.getTitle());
            preparedStatement.setString(2, book.getDescription());
            preparedStatement.setLong(3, book.getPopularity());
            preparedStatement.setString(4, book.getImage());
            preparedStatement.setString(5, book.getThumbnail());
            preparedStatement.setString(6, book.getIsbn13());
            rowAffected = preparedStatement.executeUpdate();
        }
        return rowAffected;
    }

    public static List<Book> selectBookList(boolean mostRecent, boolean mostPopular)
            throws SQLException {

        ConnectionPool connectionPool = ConnectionPool.getInstance();

        String query = "SELECT * FROM book";

        if (mostPopular) {
            query += " ORDER BY book.popularity DESC";
        } else if (mostRecent) {
            query += " ORDER BY book.pub_year DESC";
        }

        List<Book> books = new ArrayList<>();

        try (Connection connection = connectionPool.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                addBookToList(resultSet, books);
            }
        }

        return books;
    }

    public static List<Book> searchForTitle(String title) throws SQLException {
        ConnectionPool connectionPool = ConnectionPool.getInstance();

        String query = "SELECT * FROM book WHERE book.title LIKE ?";

        List<Book> books = new ArrayList<>();

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, "%" + title + "%");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                addBookToList(resultSet, books);
            }
        }

        return books;
    }

    public static List<Book> selectBookCategoryList(String category) throws SQLException {
        ConnectionPool connectionPool = ConnectionPool.getInstance();

        String query = "SELECT * FROM book, book_category " +
                "WHERE book_category.category_id = " +
                CategoryDB.selectCategoryId(category) +
                " AND book.book_id = book_category.book_id";

        List<Book> books = new ArrayList<>();

        try (Connection connection = connectionPool.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                addBookToList(resultSet, books);
            }
        }

        return books;
    }

    private static void addBookToList(ResultSet resultSet, List<Book> books) throws SQLException {
        Book book = new Book();
        long bookId = resultSet.getLong("BOOK_ID");
        book.setIsbn13(resultSet.getString("ISBN_13"));
        book.setTitle(resultSet.getString("TITLE"));
        book.setYearPublished(resultSet.getInt("PUB_YEAR"));
        book.setDescription(resultSet.getString("DESC"));
        book.setPopularity(resultSet.getLong("POPULARITY"));
        book.setImage(resultSet.getString("IMAGE"));
        book.setThumbnail(resultSet.getString("THUMB"));
        book.setFiles(BookFileDB.selectBookFiles(bookId));
        book.setPublisher(PublisherDB.selectBookPublisher(bookId));
        book.setAuthors(AuthorDB.selectBookAuthors(bookId));
        book.setCategories(CategoryDB.selectBookCategories(bookId));

        books.add(book);
    }
}
