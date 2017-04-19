package entities;

import entities.user.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;


/**
 * The persistent class for the book database table.
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "Book.findAll", query = "SELECT b FROM Book b"),
        @NamedQuery(name = "Book.findByIsbn", query = "SELECT b FROM Book b WHERE b.isbn13 = :isbn")
})
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

    @Column(name = "POPULARITY", nullable = false)
    private Long popularity;

    @Column(name = "IMAGE_PATH")
    private String imagePath;

    @Column(name = "THUMB_PATH")
    private String thumbnail;

    //bi-directional many-to-many association to Account
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "DownloadList",
            joinColumns = @JoinColumn(name = "BOOK_ID", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "USER_ID", nullable = false))
    private Set<User> users;

    //bi-directional many-to-many association to Author
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "BookAuthor",
            joinColumns = @JoinColumn(name = "BOOK_ID", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "AUTH_ID", nullable = false))
    private Set<Author> authors;

    //bi-directional many-to-many association to Category
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "BookCategory",
            joinColumns = @JoinColumn(name = "BOOK_ID", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "CATEGORY_ID", nullable = false))
    private Set<Category> categories;

    //bi-directional many-to-many association to Publisher
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "BookPublisher",
            joinColumns = @JoinColumn(name = "BOOK_ID", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "PUB_ID", nullable = false))
    private Set<Publisher> publishers;

    //bi-directional many-to-one association to BookFile
//    @OneToMany(fetch = FetchType.EAGER, mappedBy = "BOOK_FILE")
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "BOOK_ID", referencedColumnName = "ID")
    private Set<BookFile> bookFiles;

    public Book() {
    }

    public Book(String isbn13) {
        this();
        this.isbn13 = isbn13;
    }

    public Book(String isbn13, String title, int yearPublished,
                String description, Set<BookFile> files, String image,
                String thumbnail, Set<Publisher> publisher, Set<Author> authors,
                Set<Category> categories) {
        this(isbn13);
        this.setTitle(title);
        this.setPubYear(yearPublished);
        this.setDescription(description);
        this.setBookFiles(files);
        this.setImagePath(image);
        this.setThumbnail(thumbnail);
        this.setPublishers(publisher);
        this.setAuthors(authors);
        this.setCategories(categories);
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

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public Set<Publisher> getPublishers() {
        return publishers;
    }

    public void setPublishers(Set<Publisher> publishers) {
        this.publishers = publishers;
    }

    public Set<BookFile> getBookFiles() {
        return bookFiles;
    }

    public void setBookFiles(Set<BookFile> bookFiles) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;

        return isbn13 != null ? isbn13.equals(book.isbn13) : book.isbn13 == null;
    }

    @Override
    public int hashCode() {
        return isbn13 != null ? isbn13.hashCode() : 0;
    }
}