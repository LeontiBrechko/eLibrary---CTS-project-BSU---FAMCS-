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
}