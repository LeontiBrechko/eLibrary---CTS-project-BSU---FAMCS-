package entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;


/**
 * The persistent class for the author database table.
 */
@Entity
@NamedQuery(name = "Author.findAll", query = "SELECT a FROM Author a")
public class Author extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    //bi-directional many-to-many association to Book
    @ManyToMany
    @JoinTable(name = "BOOK_AUTHOR",
            joinColumns = @JoinColumn(name = "AUTH_ID", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "BOOK_ID", nullable = false))
    private List<Book> books;

    public Author() {
    }

    public Author(String firstName, String lastName) {
        this.setFirstName(firstName);
        this.setLastName(lastName);
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Book> getBooks() {
        return this.books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

}