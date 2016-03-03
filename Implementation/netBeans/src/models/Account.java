package models;

import data.AccountDB;
import models.enums.AccountRole;
import models.enums.AccountState;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by Leonti on 2016-02-27.
 */
public class Account implements Serializable {
    String login;
    private String email;
    // TODO: 2016-02-29 review account password type
    private String password;
    private AccountRole role;
    private LocalDateTime creationDate;
    private AccountState state;
    private User user;

    public Account() {
        this("", "", "", AccountRole.USER, LocalDateTime.now(), AccountState.TEPMORARY, null);
    }

    public Account(String login, String email,
                   String password, AccountRole role,
                   LocalDateTime creationDate, AccountState state, User user) {
        this.setLogin(login);
        this.setEmail(email);
        this.setPassword(password);
        this.setRole(role);
        this.setCreationDate(creationDate);
        this.setState(state);
        this.setUser(user);
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
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

    public AccountRole getRole() {
        return role;
    }

    public void setRole(AccountRole role) {
        this.role = role;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public AccountState getState() {
        return state;
    }

    public void setState(AccountState state) {
        this.state = state;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
