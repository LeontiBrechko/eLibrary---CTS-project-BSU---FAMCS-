package models;

import utils.dataValidation.DataValidationException;
import utils.dataValidation.InternalDataValidationException;
import utils.dataValidation.DataValidationUtil;

import java.io.Serializable;

/**
 * Created by Leonti on 2016-02-27.
 */
public class User implements Serializable {
    private String firstName;
    private String lastName;
    private Account account;

    public User(String firstName, String lastName, Account account)
            throws DataValidationException, InternalDataValidationException{
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setAccount(account);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) throws DataValidationException {
        if (firstName == null
                || firstName.trim().equals("")
                || DataValidationUtil.xssInjectionCheck(firstName)) {
            throw new DataValidationException("Please, enter the user first name (without < > symbols)");
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
            throw new DataValidationException("Please, enter the user last name (without < > symbols)");
        }
        this.lastName = lastName;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) throws InternalDataValidationException {
        if (account == null) {
            throw new InternalDataValidationException("Invalid user account");
        }
        this.account = account;
    }
}
