package models;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Leonti on 2016-02-27.
 */

@Entity
public class User implements Serializable {
    @Id
    @Column(name = "USER_ID")
    private long id;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @OneToOne(cascade = CascadeType.ALL, optional = false, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "ACCOUNT_ID")
    private Account account;

    public User() {
        this.setFirstName("");
        this.setLastName("");
        this.account = new Account();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
