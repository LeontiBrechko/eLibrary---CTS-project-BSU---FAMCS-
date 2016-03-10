package data;

import models.Book;
import models.BookFile;
import models.enums.Format;
import models.enums.Language;
import utils.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leonti on 2016-03-07.
 */
public class BookFileDB {
    public static List<BookFile> selectBookFiles(long bookId) throws SQLException {
        ConnectionPool connectionPool = ConnectionPool.getInstance();

        String query = "SELECT format.name, language.name, book_file.path\n" +
                "FROM format, language, book_file\n" +
                "WHERE format.format_id = book_file.format_id\n" +
                "AND language.lang_id = book_file.lang_id\n" +
                "AND book_file.book_id = ?";

        List<BookFile> files = new ArrayList<>();

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, bookId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                BookFile file = new BookFile();
                file.setFormat(Format.valueOf(resultSet.getString(1)));
                file.setLanguage(Language.valueOf(resultSet.getString(2)));
                file.setPath(resultSet.getString(3));
                files.add(file);
            }
        }

        return files;
    }
}
