package models;

import models.enums.AccountRole;
import models.enums.AccountState;
import utils.LocalDateTimeAttributeConverter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by Leonti on 2016-02-27.
 */

@Entity
public class Account implements Serializable {
    @Id
    @Column(name = "ACCOUNT_ID", unique = true, nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, nullable = false, updatable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    // TODO: 2016-02-29 review account password type
    @Column(nullable = false)
    private String password;

    @Column(name = "SALT_VALUE")
    private String saltValue;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountRole role;

    @Temporal(TemporalType.TIMESTAMP)
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    @Column(name = "CREATION_TIME", nullable = false)
    private LocalDateTime creationTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountState state;

    public Account() {
        this.setUsername("");
        this.setEmail("");
        this.setPassword("");
        this.setRole(AccountRole.USER);
        this.setCreationTime(LocalDateTime.now());
        this.setState(AccountState.TEMPORARY);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String login) {
        this.username = login;
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

    public String getSaltValue() {
        return saltValue;
    }

    public void setSaltValue(String saltValue) {
        this.saltValue = saltValue;
    }

    public AccountRole getRole() {
        return role;
    }

    public void setRole(AccountRole role) {
        this.role = role;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public AccountState getState() {
        return state;
    }

    public void setState(AccountState state) {
        this.state = state;
    }
}
