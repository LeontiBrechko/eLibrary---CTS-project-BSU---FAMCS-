package utils;

import models.Account;
import models.Book;
import models.BookFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by Leonti on 2016-03-17.
 */
public class ZipUtil {
    public static String createDownloadsZip(List<Book> books, ServletContext context)
            throws IOException {
        try (ZipOutputStream zipOutputStream =
                     new ZipOutputStream(new FileOutputStream(context.getRealPath("/test.zip")))) {
            File file;
            byte[] buffer;
            int read;

            for (Book book : books) {
                for (BookFile bookFile : book.getFiles()) {
                    file = new File(context.getRealPath(bookFile.getPath()));
                    try (FileInputStream fileInputStream = new FileInputStream(file)) {
                        zipOutputStream.putNextEntry(new ZipEntry(file.getName()));

                        buffer = new byte[1024];
                        while ((read = fileInputStream.read(buffer)) != -1) {
                            zipOutputStream.write(buffer, 0, read);
                        }
                        zipOutputStream.closeEntry();
                    }
                }
            }
        }
        return "test.zip";
    }
}
