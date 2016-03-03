package models;

import java.io.Serializable;

/**
 * Created by Leonti on 2016-02-27.
 */
public class User implements Serializable {
    private String firstName;
    private String lastName;

    public User() {
        this("", "");
    }

    public User(String firstName, String lastName) {
        this.setFirstName(firstName);
        this.setLastName(lastName);
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
}
