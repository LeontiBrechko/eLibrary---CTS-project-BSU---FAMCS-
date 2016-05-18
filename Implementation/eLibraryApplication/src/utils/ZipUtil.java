package utils;

import data.BookDB;
import models.Account;
import models.Book;
import models.BookFile;
import utils.dataValidation.InternalDataValidationException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by Leonti on 2016-03-17.
 */
public class ZipUtil {
    public static String createDownloadsZip(Account account, ServletContext context)
            throws IOException, InternalDataValidationException, SQLException {
        String path =
                context.getRealPath("/download/usersDownloads/" +
                        account.getUsername() +
                        "/" + LocalDateTime.now().toString() + ".zip");
        File outputFile = new File(path);
        outputFile.getParentFile().mkdirs();
        outputFile.createNewFile();
        try (ZipOutputStream zipOutputStream =
                     new ZipOutputStream(new FileOutputStream(outputFile))) {
            File sourceFile;
            byte[] buffer;
            int read;

            for (Book book : account.getDownloadList()) {
                book.setPopularity(book.getPopularity() + 1);
                BookDB.updateBook(book);
                for (BookFile bookFile : book.getFiles()) {
                    sourceFile = new File(context.getRealPath(bookFile.getPath()));
                    try (FileInputStream fileInputStream = new FileInputStream(sourceFile)) {
                        zipOutputStream.putNextEntry(new ZipEntry(sourceFile.getName()));

                        buffer = new byte[1024];
                        while ((read = fileInputStream.read(buffer)) != -1) {
                            zipOutputStream.write(buffer, 0, read);
                        }
                        zipOutputStream.closeEntry();
                    }
                }
            }
        }
        return path;
    }
}
