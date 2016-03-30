package models;

import utils.dataValidation.DataValidationException;
import utils.dataValidation.DataValidationUtil;

import java.io.Serializable;

/**
 * Created by Leonti on 2016-03-04.
 */
public class Author implements Serializable {
    private String firstName;
    private String lastName;

    public Author(String firstName, String lastName) throws DataValidationException {
        this.setFirstName(firstName);
        this.setLastName(lastName);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) throws DataValidationException {
        if (firstName == null
                || firstName.trim().equals("")
                || DataValidationUtil.xssInjectionCheck(firstName)) {
            throw new DataValidationException("Please, enter the author`s first name (without < > symbols)");
        }
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) throws DataValidationException {
        if (lastName == null
                || lastName.trim().equals("")
                || DataValidationUtil.xssInjectionCheck(lastName)) {
            throw new DataValidationException("Please, enter the author`s last name (without < > symbols)");
        }
        this.lastName = lastName;
    }
}
