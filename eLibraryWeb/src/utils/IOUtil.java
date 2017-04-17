package utils;

import entities.Book;
import entities.BookFile;
import entities.user.User;
import service.LibraryService;
import utils.validation.InternalDataValidationException;

import javax.servlet.ServletContext;
import javax.servlet.http.Part;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class IOUtil {
    public static String readDescriptionFile(String path) throws IOException {
        StringBuilder description = new StringBuilder();
        Path descriptionPath = Paths.get(path).toAbsolutePath();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(descriptionPath.toFile()))) {
            String nextLine;
            while ((nextLine = bufferedReader.readLine()) != null) {
                description.append(nextLine);
            }
        }

        return description.toString();
    }

    public static void writeFileProperty(Part filePart, String path) throws IOException {
        File file = new File(path);
        file.getParentFile().mkdirs();
        file.createNewFile();
        try (InputStream inputStream = filePart.getInputStream();
             BufferedOutputStream bufferedOutputStream =
                     new BufferedOutputStream(new FileOutputStream(file))) {
            byte[] buffer = new byte[1024];
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                bufferedOutputStream.write(buffer, 0, read);
            }
        }
    }

    public static String createDownloadsZip(User user, ServletContext context, LibraryService service)
            throws IOException, InternalDataValidationException, SQLException {
        String path =
                context.getRealPath("/download/usersDownloads/" +
                        user.getUsername() +
                        "/" + LocalDateTime.now().toString() + ".zip");
        File outputFile = new File(path);
        outputFile.getParentFile().mkdirs();
        outputFile.createNewFile();
        try (ZipOutputStream zipOutputStream =
                     new ZipOutputStream(new FileOutputStream(outputFile))) {
            File sourceFile;
            byte[] buffer;
            int read;

            for (Book book : user.getBooks()) {
                book.setPopularity(book.getPopularity() + 1);
                service.saveBook(book);
                for (BookFile bookFile : book.getBookFiles()) {
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
