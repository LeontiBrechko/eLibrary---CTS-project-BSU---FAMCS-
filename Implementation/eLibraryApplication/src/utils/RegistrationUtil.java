package utils;

import data.AccountDB;
import data.UserDB;
import models.Account;
import models.User;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

/**
 * Created by Leonti on 2016-03-21.
 */
public class RegistrationUtil {
    public static boolean checkRegistrationData(
            User user, HttpServletRequest req, HttpServletResponse resp) {
        boolean flag = true;

        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String email = user.getAccount().getEmail();
        String username = user.getAccount().getUsername();
        String password = user.getAccount().getPassword();
        String confirmPassword = req.getParameter("confirmPassword");

        if (firstName == null || firstName.equals("")) {
            flag = false;
            req.setAttribute("invalidFirstName", true);
        }
        if (lastName == null || lastName.equals("")) {
            flag = false;
            req.setAttribute("invalidLastName", true);
        }
        if (!isValidEmail(email)) {
            flag = false;
            req.setAttribute("invalidEmail", true);
            req.setAttribute("invalidEmailMessage", "Please, enter valid email.");
        }
        if (username == null || username.equals("")) {
            flag = false;
            req.setAttribute("invalidUsername", true);
            req.setAttribute("invalidUMessage", "Please, fill in this field.");
        }
        // TODO: 2016-02-29 review account password validation
        if (password == null || password.equals("") ||
                password.length() < 6) {
            flag = false;
            req.setAttribute("invalidPassword", true);
            req.setAttribute("invalidPasswordMessage", "Password must be at least 6 characters.");
        } else if (confirmPassword == null || !password.equals(confirmPassword)) {
            flag = false;
            req.setAttribute("invalidConfirmPassword", true);
            req.setAttribute("invalidConfirmPasswordMessage", "Confirmation password must match password.");
        }

        if (!flag) {
            req.setAttribute("message", "Please, fill in this field.");
        }

        return flag;
    }

    // TODO: 2016-02-29 review account email validation
    public static boolean isValidEmail(String email) {
        boolean flag = true;

        try {
            InternetAddress emailAddress = new InternetAddress(email);
            emailAddress.validate();
        } catch (AddressException e) {
            flag = false;
        }

        return flag;
    }

    public static boolean accountExists(Account account, HttpServletRequest req, HttpServletResponse resp)
            throws SQLException {
        boolean flag = false;

        String username = account.getUsername();
        String email = account.getEmail();

        if (AccountDB.emailExists(email)) {
            flag = true;
            req.setAttribute("invalidEmail", true);
            req.setAttribute("invalidEmailMessage", "The email is already registered.");
        }
        if (AccountDB.usernameExists(username)) {
            flag = true;
            req.setAttribute("invalidUsername", true);
            req.setAttribute("invalidUsernameMessage", "The username is already used.");
        }

        return flag;
    }

    public static void createUserAccount(User user)
            throws NoSuchAlgorithmException, SQLException {
        String plainPassword = user.getAccount().getPassword();
        String saltValue = PasswordUtil.getSalt();
        String saltedAndHashedPassword = PasswordUtil.hashPassword(plainPassword + saltValue);
        String confimrationToken = PasswordUtil.getConfirmationToken();
        user.getAccount().setPassword(saltedAndHashedPassword);
        user.getAccount().setSaltValue(saltValue);
        user.getAccount().setConfirmationToken(confimrationToken);
        UserDB.insertUser(user);
    }

    // TODO: 2016-02-29 implement email confirmation method in registration controller
    public static void sendConfirmationEmail(String referrer, Account account)
            throws MessagingException {
        // TODO: 2016-03-11 implement forwarding accordingly to referrer
        MailUtil.sendMail(account.getEmail(), "", "", "", true);
    }
}
