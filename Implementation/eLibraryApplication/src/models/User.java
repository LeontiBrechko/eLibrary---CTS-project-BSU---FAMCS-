package models;

import java.io.Serializable;

/**
 * Created by Leonti on 2016-02-27.
 */
public class User implements Serializable {
    private String firstName;
    private String lastName;
    private Account account;

    public User() {
        this.setFirstName("");
        this.setLastName("");
        this.account = new Account();
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
