package models;

import java.io.Serializable;

/**
 * Created by Leonti on 2016-03-04.
 */
public class Author implements Serializable{
    private String firstName;
    private String lastName;

    public Author() {
        this.setFirstName("");
        this.setLastName("");
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
