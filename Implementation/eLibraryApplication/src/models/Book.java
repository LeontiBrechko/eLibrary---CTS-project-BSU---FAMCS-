package models;

import utils.dataValidation.DataValidationException;
import utils.dataValidation.InternalDataValidationException;
import utils.dataValidation.DataValidationUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by Leonti on 2016-03-04.
 */
public class Book implements Serializable {

    private String isbn13;
    private String title;
    private int yearPublished;
    private String description;
    private long popularity;
    private List<BookFile> files;
    private String image;
    private String thumbnail;
    private Publisher publisher;
    private List<Author> authors;
    private List<Category> categories;

    public Book(String isbn13, String title, int yearPublished,
                String description, List<BookFile> files, String image,
                String thumbnail, Publisher publisher, List<Author> authors,
                List<Category> categories)
            throws DataValidationException, InternalDataValidationException {
        // TODO: 2016-03-07 review default image url
        this.setIsbn13(isbn13);
        this.setTitle(title);
        this.setYearPublished(yearPublished);
        this.setDescription(description);
        this.setFiles(files);
        this.setImage(image);
        this.setThumbnail(thumbnail);
        this.setPublisher(publisher);
        this.setAuthors(authors);
        this.setCategories(categories);
    }

    public String getIsbn13() {
        return isbn13;
    }

    public void setIsbn13(String isbn13) throws DataValidationException {
        if (isbn13 == null
                || isbn13.trim().equals("")
                || isbn13.length() != 13
                || DataValidationUtil.xssInjectionCheck(isbn13)) {
            throw new DataValidationException("Please, enter the book isbn 13 " +
                    "(without < > symbols and exactly 13 characters length)");
        }
        this.isbn13 = isbn13;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) throws DataValidationException {
        if (title == null
                || title.trim().equals("")
                || DataValidationUtil.xssInjectionCheck(title)) {
            throw new DataValidationException("Please, enter the book title (without < > symbols)");
        }
        this.title = title;
    }

    public int getYearPublished() {
        return yearPublished;
    }

    public void setYearPublished(int yearPublished) throws DataValidationException {
        if (yearPublished < 0) {
            throw new DataValidationException("Please, enter valid book publication year (grater than 0)");
        }
        this.yearPublished = yearPublished;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) throws InternalDataValidationException {
        if (description == null) {
            description = "";
        }
        if (DataValidationUtil.xssInjectionCheck(description)) {
            throw new InternalDataValidationException("Invalid description path");
        }
        this.description = description;
    }

    public long getPopularity() {
        return popularity;
    }

    public void setPopularity(long popularity) throws InternalDataValidationException {
        if (popularity < 0) {
            throw new InternalDataValidationException("Invalid book popularity value");
        }
        this.popularity = popularity;
    }

    public List<BookFile> getFiles() {
        return files;
    }

    public void setFiles(List<BookFile> files) throws InternalDataValidationException {
        if (files == null) {
            throw new InternalDataValidationException("Invalid book files list");
        }
        this.files = files;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        if (image == null) {
            image = "";
        }
        this.image = image;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        if (thumbnail == null) {
            thumbnail = "";
        }
        this.thumbnail = thumbnail;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) throws InternalDataValidationException {
        if (publisher == null) {
            throw new InternalDataValidationException("Invalid book publisher");
        }
        this.publisher = publisher;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) throws InternalDataValidationException {
        if (authors == null) {
            throw new InternalDataValidationException("Invalid book authors list");
        }
        this.authors = authors;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) throws InternalDataValidationException {
        if (categories == null) {
            throw new InternalDataValidationException("Invalid book categories list");
        }
        this.categories = categories;
    }

    public String readDescriptionFile(String path) throws IOException {
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

    public void writeFileProperty(Part filePart, String path) throws IOException {
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

    public int getBookFileListIndex(BookFile bookFile) {
        int flag = -1;
        for (int i = 0; i < this.files.size(); i++) {
            if (bookFile.getFormat().name().equals(this.files.get(i).getFormat().name()) &&
                    bookFile.getLanguage().name().equals(this.files.get(i).getLanguage().name())) {
                flag = i;
                break;
            }
        }
        return flag;
    }

    public static Book getBookToUpdate(HttpServletRequest req) {
        HttpSession session = req.getSession();
        final Object lock = session.getId().intern();

        Book book;

        synchronized (lock) {
            book = (Book) session.getAttribute("bookToUpdate");
        }

        return book;
    }

    public static void setBookToUpdate(Book book,
                                         HttpServletRequest req) {
        HttpSession session = req.getSession();
        final Object lock = session.getId().intern();

        synchronized (lock) {
            session.setAttribute("bookToUpdate", book);
        }
    }

    public static void deleteBookToUpdate(HttpServletRequest req) {
        HttpSession session = req.getSession();
        final Object lock = session.getId().intern();

        synchronized (lock) {
            session.removeAttribute("bookToUpdate");
        }
    }
}
