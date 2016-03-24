package data;

import com.sun.xml.internal.bind.v2.TODO;
import models.Book;
import models.Category;
import utils.ConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leonti on 2016-03-04.
 */
public class BookDB {
    public static Book selectBook(String isbn13)
            throws SQLException {
        ConnectionPool connectionPool = ConnectionPool.getInstance();

        String query = "SELECT * FROM book WHERE isbn_13 = ?";

        Book book = null;

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, isbn13);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                List<Book> temp = new ArrayList<>(1);
                addBookToList(resultSet, temp);
                if (temp.size() >= 1) {
                    book = temp.get(0);
                }
            }
        }

        return book;
    }

    public static Book selectBook(long bookId)
            throws SQLException {
        ConnectionPool connectionPool = ConnectionPool.getInstance();

        String query = "SELECT * FROM book WHERE book_id = ?";

        Book book = null;

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, bookId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                List<Book> temp = new ArrayList<>(1);
                addBookToList(resultSet, temp);
                if (temp.size() >= 1) {
                    book = temp.get(0);
                }
            }
        }

        return book;
    }

    public static int updateBook(Book book)
            throws SQLException {
        ConnectionPool connectionPool = ConnectionPool.getInstance();

        String query = "UPDATE book SET " +
                "book.title = ?, " +
                "book.description = ?, " +
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
            rowAffected += preparedStatement.executeUpdate();
        }
        return rowAffected;
    }

    public static int insertBook(Book book)
            throws SQLException {
        ConnectionPool connectionPool = ConnectionPool.getInstance();

        String query = "INSERT INTO book (isbn_13, title, pub_year, description, popularity, image, thumb) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        int rowAffected;

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, book.getIsbn13());
                preparedStatement.setString(2, book.getTitle());
                preparedStatement.setInt(3, book.getYearPublished());
                preparedStatement.setString(4, book.getDescription());
                preparedStatement.setLong(5, book.getPopularity());
                preparedStatement.setString(6, book.getImage());
                preparedStatement.setString(7, book.getThumbnail());
                rowAffected = preparedStatement.executeUpdate();
            }

            long id = selectBookId(connection, book.getIsbn13());
            rowAffected += BookFileDB.insertBookFiles(connection, id, book);
            rowAffected += PublisherDB.insertBookPublisher(connection, id, book);
            rowAffected += AuthorDB.insertBookAuthors(connection, id, book);
            rowAffected += CategoryDB.insertBookCategories(connection, id, book);
        }

        return rowAffected;
    }

    public static int deleteBook(String isbn13)
            throws SQLException {
        ConnectionPool connectionPool = ConnectionPool.getInstance();

        String query = "DELETE FROM book WHERE isbn13 = ?";

        int rowAffected = 0;

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            long id = selectBookId(connection, isbn13);
            rowAffected += BookFileDB.deleteBookFiles(connection, id);
            rowAffected += PublisherDB.deleteBookPublisher(connection, id);
            rowAffected += AuthorDB.deleteBookAuthors(connection, id);
            rowAffected += CategoryDB.deleteBookCategories(connection, id);
            rowAffected += deleteBookFromDownloadLists(connection, id);

            preparedStatement.setString(1, isbn13);
            rowAffected += preparedStatement.executeUpdate();
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
        } else {
            query += " ORDER BY book.book_id";
        }

        List<Book> books = new ArrayList<>();

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                addBookToList(resultSet, books);
            }
        }

        return books;
    }

    public static long[] selectBookIds(Connection connection, List<String> bookIsbn13List)
            throws SQLException {
        long[] ids = new long[bookIsbn13List.size()];

        for (int i = 0; i < bookIsbn13List.size(); i++) {
            ids[i] = selectBookId(connection, bookIsbn13List.get(i));
        }

        return ids;
    }


    public static List<Book> searchForTitle(String title)
            throws SQLException {
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

    public static List<Book> selectBookCategoryList(Category category)
            throws SQLException {
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

    private static long selectBookId(Connection connection, String isbn13)
            throws SQLException {
        String query = "SELECT book_id FROM book WHERE isbn_13 = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, isbn13);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getLong(1);
            } else {
                throw new SQLException("Cannot select book id for isbn13:  " +
                        isbn13);
            }
        }
    }

    private static void addBookToList(ResultSet resultSet, List<Book> books)
            throws SQLException {
        Book book = new Book();
        long bookId = resultSet.getLong("BOOK_ID");
        book.setIsbn13(resultSet.getString("ISBN_13"));
        book.setTitle(resultSet.getString("TITLE"));
        book.setYearPublished(resultSet.getInt("PUB_YEAR"));
        book.setDescription(resultSet.getString("DESCRIPTION"));
        book.setPopularity(resultSet.getLong("POPULARITY"));
        book.setImage(resultSet.getString("IMAGE"));
        book.setThumbnail(resultSet.getString("THUMB"));
        book.setFiles(BookFileDB.selectBookFiles(bookId));
        book.setPublisher(PublisherDB.selectBookPublisher(bookId));
        book.setAuthors(AuthorDB.selectBookAuthors(bookId));
        book.setCategories(CategoryDB.selectBookCategories(bookId));

        books.add(book);
    }

    private static int deleteBookFromDownloadLists(Connection connection, long bookId)
            throws SQLException{
        String query = "DELETE FROM account_download_list WHERE book_id = ?";

        int rowAffected = 0;

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, bookId);
            rowAffected = preparedStatement.executeUpdate();
        }

        return rowAffected;
    }
}
