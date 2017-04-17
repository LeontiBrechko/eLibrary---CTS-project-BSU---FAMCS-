package entities.user;

import entities.BaseEntity;
import entities.Book;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * The persistent class for the user database table.
 */
@Entity
@NamedQuery(name = "User.findAll", query = "SELECT u FROM User u")
public class User extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "FIRST_NAME", nullable = false)
    private String firstName;

    @Column(name = "LAST_NAME", nullable = false)
    private String lastName;

    @Column(name = "USERNAME", nullable = false, unique = true)
    private String username;

    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATION_TIME", nullable = false)
    private Date creationTime;

    @Column(name = "CONFIRM_TOKEN", nullable = false)
    private String confirmToken;

    @Column(name = "SALT_VALUE", nullable = false)
    private String saltValue;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
    private List<UserRole> roles;

    @Column(name = "STATE")
    @Enumerated(EnumType.STRING)
    private UserState state;

    //bi-directional many-to-many association to Book
    @ManyToMany
    @JoinTable(name = "DOWNLOAD_LIST",
            joinColumns = @JoinColumn(name = "USER_ID", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "BOOK_ID", nullable = false))
    private List<Book> books;

    public User() {
    }

    public User(String firstName, String lastName, String username,
                String email, String password, String confirmToken, String saltValue) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.confirmToken = confirmToken;
        this.saltValue = saltValue;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public String getConfirmToken() {
        return confirmToken;
    }

    public void setConfirmToken(String confirmToken) {
        this.confirmToken = confirmToken;
    }

    public String getSaltValue() {
        return saltValue;
    }

    public void setSaltValue(String saltValue) {
        this.saltValue = saltValue;
    }

    public List<UserRole> getRoles() {
        return roles;
    }

    public void setRoles(List<UserRole> roles) {
        this.roles = roles;
    }

    public UserState getState() {
        return state;
    }

    public void setState(UserState state) {
        this.state = state;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}