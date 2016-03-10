package models;

import models.enums.Format;
import models.enums.Language;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public Book() {
        // TODO: 2016-03-07 review default image url
        this.setImage("");
        this.setThumbnail("");
    }

    public String getIsbn13() {
        return isbn13;
    }

    public void setIsbn13(String isbn13) {
        this.isbn13 = isbn13;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYearPublished() {
        return yearPublished;
    }

    public void setYearPublished(int yearPublished) {
        this.yearPublished = yearPublished;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getPopularity() {
        return popularity;
    }

    public void setPopularity(long popularity) {
        this.popularity = popularity;
    }

    public List<BookFile> getFiles() {
        return files;
    }

    public void setFiles(List<BookFile> files) {
        this.files = files;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public void readDescription() throws IOException {
        StringBuilder description = new StringBuilder();
        Path descriptionPath = Paths.get(this.getDescription()).toAbsolutePath();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(descriptionPath.toFile()))) {
            String nextLine;
            while ((nextLine = bufferedReader.readLine()) != null) {
                description.append(nextLine);
            }
        }

        this.setDescription(description.toString());
    }
}
