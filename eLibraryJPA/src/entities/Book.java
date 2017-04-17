package entities;

import entities.user.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;


/**
 * The persistent class for the book database table.
 */
@Entity
@NamedQuery(name = "Book.findAll", query = "SELECT b FROM Book b")
public class Book extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "ISBN_13", unique = true, nullable = false)
    private String isbn13;

    @Column(name = "TITLE", nullable = false)
    private String title;

    @Column(name = "PUB_YEAR")
    private int pubYear;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "POPULARITY",
            columnDefinition = "signed bigint DEFAULT 0",
            nullable = false)
    private Long popularity;

    @Column(name = "IMAGE_PATH")
    private String imagePath;

    @Column(name = "THUMB_PATH")
    private String thumb;

    //bi-directional many-to-many association to Account
    @ManyToMany
    @JoinTable(name = "DOWNLOAD_LIST",
            joinColumns = @JoinColumn(name = "BOOK_ID", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "USER_ID", nullable = false))
    private List<User> users;

    //bi-directional many-to-many association to Author
    @ManyToMany
    @JoinTable(name = "BOOK_AUTHOR",
            joinColumns = @JoinColumn(name = "BOOK_ID", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "AUTH_ID", nullable = false))
    private List<Author> authors;

    //bi-directional many-to-many association to Category
    @ManyToMany
    @JoinTable(name = "BOOK_CATEGORY",
            joinColumns = @JoinColumn(name = "BOOK_ID", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "CATEGORY_ID", nullable = false))
    private List<Category> categories;

    //bi-directional many-to-many association to Publisher
    @ManyToMany
    @JoinTable(name = "BOOK_PUBLISHER",
            joinColumns = @JoinColumn(name = "BOOK_ID", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "PUB_ID", nullable = false))
    private List<Publisher> publishers;

    //bi-directional many-to-one association to BookFile
//    @OneToMany(fetch = FetchType.EAGER, mappedBy = "BOOK_FILE")
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "BOOK_ID", referencedColumnName = "ID")
    private List<BookFile> bookFiles;

    public Book() {
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

    public int getPubYear() {
        return pubYear;
    }

    public void setPubYear(int pubYear) {
        this.pubYear = pubYear;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getPopularity() {
        return popularity;
    }

    public void setPopularity(Long popularity) {
        this.popularity = popularity;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
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

    public List<Publisher> getPublishers() {
        return publishers;
    }

    public void setPublishers(List<Publisher> publishers) {
        this.publishers = publishers;
    }

    public List<BookFile> getBookFiles() {
        return bookFiles;
    }

    public void setBookFiles(List<BookFile> bookFiles) {
        this.bookFiles = bookFiles;
    }

    public BookFile addBookFile(BookFile bookFile) {
        getBookFiles().add(bookFile);
        bookFile.setBook(this);

        return bookFile;
    }

    public BookFile removeBookFile(BookFile bookFile) {
        getBookFiles().remove(bookFile);
        bookFile.setBook(null);

        return bookFile;
    }

}