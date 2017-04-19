package entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;


/**
 * The persistent class for the author database table.
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "Author.findAll", query = "SELECT a FROM Author a"),
        @NamedQuery(name = "Author.findByName", query = "SELECT a FROM Author a " +
                "WHERE a.firstName = :firstName AND a.lastName = :lastName")
})
public class Author extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    //bi-directional many-to-many association to Book
    @ManyToMany
    @JoinTable(name = "BookAuthor",
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Author author = (Author) o;

        if (firstName != null ? !firstName.equals(author.firstName) : author.firstName != null) return false;
        return lastName != null ? lastName.equals(author.lastName) : author.lastName == null;
    }

    @Override
    public int hashCode() {
        int result = firstName != null ? firstName.hashCode() : 0;
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        return result;
    }
}