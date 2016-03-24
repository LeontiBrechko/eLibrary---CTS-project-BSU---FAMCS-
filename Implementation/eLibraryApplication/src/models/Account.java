package models;

import data.AccountDB;
import models.enums.AccountRole;
import models.enums.AccountState;
import utils.CookieUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leonti on 2016-02-27.
 */
public class Account implements Serializable {
    private String username;
    private String email;
    private String password;
    private String saltValue;
    private AccountRole role;
    private LocalDateTime creationTime;
    private AccountState state;
    private List<Book> downloadList;
    private String confirmationToken;



    public Account() {
        this.setUsername("");
        this.setEmail("");
        this.setPassword("");
        this.setRole(AccountRole.USER);
        this.setCreationTime(LocalDateTime.now());
        this.setState(AccountState.TEMPORARY);
        this.setDownloadList(new ArrayList<>());
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

    public List<Book> getDownloadList() {
        return downloadList;
    }

    public void setDownloadList(List<Book> downloadList) {
        this.downloadList = downloadList;
    }

    public String getConfirmationToken() {
        return confirmationToken;
    }

    public void setConfirmationToken(String confirmationToken) {
        this.confirmationToken = confirmationToken;
    }

    public int listContainsBook(Book book) {
        int flag = -1;
        String isbn13 = book.getIsbn13();
        for (int i = 0; i < this.downloadList.size(); i++) {
            if (this.downloadList.get(i).getIsbn13().equals(isbn13)) {
                flag = i;
                break;
            }
        }
        return flag;
    }

    public static Account getCookieAccount(HttpServletRequest req)
            throws SQLException {
        Cookie[] cookies = req.getCookies();
        String username = CookieUtil.getCookieValue(cookies, "username");

        Account account = null;

        if (username != null && !username.equals("")) {
            account = AccountDB.selectAccount(username);
        }

        return account;
    }

    public static void addAccountCookie(Account account,
                                        HttpServletResponse resp) {

        Cookie cookie = new Cookie("username", account.getUsername());
        cookie.setMaxAge(60 * 60 * 24 * 365 * 2);
        cookie.setPath("/");

        resp.addCookie(cookie);
    }

    public static void deleteAccountCookie(HttpServletResponse resp) {

        Cookie cookie = new Cookie("username", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");

        resp.addCookie(cookie);
    }


    public static Account getSessionAccount(HttpServletRequest req) {
        HttpSession session = req.getSession();
        final Object lock = session.getId().intern();

        Account account;

        synchronized (lock) {
            account = (Account) session.getAttribute("account");
        }

        return account;
    }

    public static void setSessionAccount(Account account,
                                         HttpServletRequest req) {
        HttpSession session = req.getSession();
        final Object lock = session.getId().intern();

        synchronized (lock) {
            session.setAttribute("account", account);
        }
    }

    public static void deleteSessionAccount(HttpServletRequest req) {
        HttpSession session = req.getSession();
        final Object lock = session.getId().intern();

        synchronized (lock) {
            session.removeAttribute("account");
        }
    }
}
