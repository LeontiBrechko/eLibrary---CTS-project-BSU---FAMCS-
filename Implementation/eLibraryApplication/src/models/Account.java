package models;

import data.AccountDB;
import models.enums.AccountRole;
import models.enums.AccountState;
import utils.CookieUtil;
import utils.dataValidation.DataValidationException;
import utils.dataValidation.InternalDataValidationException;
import utils.dataValidation.DataValidationUtil;

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


    public Account(String username, String email, String password,
                   String saltValue, String confirmationToken)
            throws DataValidationException, InternalDataValidationException {
        this.setUsername(username);
        this.setEmail(email);
        this.setPassword(password);
        this.setSaltValue(saltValue);
        this.setRole(AccountRole.USER);
        this.setCreationTime(LocalDateTime.now());
        this.setState(AccountState.TEMPORARY);
        this.setDownloadList(new ArrayList<>());
        this.setConfirmationToken(confirmationToken);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) throws DataValidationException {
        if (username == null
                || username.trim().equals("")
                || DataValidationUtil.xssInjectionCheck(username)) {
            throw new DataValidationException("Please, enter the valid username (without < > symbols)");
        }
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws DataValidationException {
        // TODO: 2016-03-29 proper email validation
//        RegistrationUtil.isValidEmail(email);
        if (email == null
                || email.trim().equals("")
                || DataValidationUtil.xssInjectionCheck(email)) {
            throw new DataValidationException("Please, enter the valid email (without < > symbols)");
        }
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) throws DataValidationException {
        // TODO: 2016-03-29 proper password validation
        if (password == null
                || password.trim().equals("")) {
            throw new DataValidationException("Please, enter the valid password");
        }
        this.password = password;
    }

    public String getSaltValue() {
        return saltValue;
    }

    public void setSaltValue(String saltValue) throws InternalDataValidationException {
        if (saltValue == null
                || saltValue.trim().equals("")) {
            throw new InternalDataValidationException("Invalid salt value");
        }
        this.saltValue = saltValue;
    }

    public AccountRole getRole() {
        return role;
    }

    public void setRole(AccountRole role) throws InternalDataValidationException {
        if (role == null) {
            throw new InternalDataValidationException("Invalid account role value");
        }
        this.role = role;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) throws InternalDataValidationException {
        if (creationTime == null) {
            throw new InternalDataValidationException("Invalid creation time value");
        }
        this.creationTime = creationTime;
    }

    public AccountState getState() {
        return state;
    }

    public void setState(AccountState state) throws InternalDataValidationException {
        if (state == null) {
            throw new InternalDataValidationException("Invalid account state value");
        }
        this.state = state;
    }

    public List<Book> getDownloadList() {
        return downloadList;
    }

    public void setDownloadList(List<Book> downloadList) throws InternalDataValidationException {
        if (saltValue == null) {
            throw new InternalDataValidationException("Invalid download list value");
        }

        this.downloadList = downloadList;
    }

    public String getConfirmationToken() {
        return confirmationToken;
    }

    public void setConfirmationToken(String confirmationToken) throws InternalDataValidationException {
        if (saltValue == null
                || confirmationToken.trim().equals("")) {
            throw new InternalDataValidationException("Invalid confirmation token value");
        }
        this.confirmationToken = confirmationToken;
    }

    public int listContainsBook(String isbn13) {
        int flag = -1;
        for (int i = 0; i < this.downloadList.size(); i++) {
            if (this.downloadList.get(i).getIsbn13().equals(isbn13)) {
                flag = i;
                break;
            }
        }
        return flag;
    }

    public static Account getCookieAccount(HttpServletRequest req)
            throws SQLException, DataValidationException, InternalDataValidationException {
        Cookie[] cookies = req.getCookies();
        String username = CookieUtil.getCookieValue(cookies, "username");

        Account account = null;

        if (username != null && !username.trim().equals("")) {
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
