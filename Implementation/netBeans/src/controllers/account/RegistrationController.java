package controllers.account;

import data.AccountDB;
import data.UserDB;
import models.Account;
import models.User;
import utils.PasswordUtil;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by Leonti on 2016-02-27.
 */

@WebServlet(name = "accountRegistration", urlPatterns = "/account/registration")
public class RegistrationController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String referrer = req.getParameter("referrer");
        String url;

        User user = initializeUserAccount(req.getParameterMap());

        try {
            if (checkRegistrationData(user, req, resp)) {
                if (!accountExists(user.getAccount(), req, resp)) {
                    createUserAccount(user);
                    Account.addAccountCookie(user.getAccount(), resp);
                    sendConfirmationEmail(user.getAccount().getEmail());
                    // TODO: 2016-03-11 implement forwarding accordingly to referrer
                    url = "/account/confirm.jsp";
                } else {
                    // TODO: 2016-03-11 error message
                    url = "/account/register.jsp";
                }
            } else {
                // TODO: 2016-03-11 error message
                url = "/account/register.jsp";
            }
        } catch (SQLException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            // TODO: 2016-03-11 error message
            url = "/index.jsp";
        }

        getServletContext().getRequestDispatcher(url).forward(req, resp);
    }

    private User initializeUserAccount(Map<String, String[]> parameters) {
        User user = new User();

        user.getAccount().setUsername(parameters.get("username")[0]);
        user.getAccount().setEmail(parameters.get("email")[0]);
        user.getAccount().setPassword(parameters.get("password")[0]);
        user.setFirstName(parameters.get("firstName")[0]);
        user.setLastName(parameters.get("lastName")[0]);

        return user;
    }

    private boolean checkRegistrationData(
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
    private boolean isValidEmail(String email) {
        boolean flag = true;

        try {
            InternetAddress emailAddress = new InternetAddress(email);
            emailAddress.validate();
        } catch (AddressException e) {
            flag = false;
        }

        return flag;
    }

    private boolean accountExists(Account account, HttpServletRequest req, HttpServletResponse resp)
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
            req.setAttribute("invalidLogin", true);
            req.setAttribute("invalidLoginMessage", "The username is already used.");
        }

        return flag;
    }

    private void createUserAccount(User user)
            throws NoSuchAlgorithmException, SQLException {
        String plainPassword = user.getAccount().getPassword();
        String saltValue = PasswordUtil.getSalt();
        String saltedAndHashedPassword = PasswordUtil.hashPassword(plainPassword + saltValue);
        user.getAccount().setPassword(saltedAndHashedPassword);
        user.getAccount().setSaltValue(saltValue);
        UserDB.insertUser(user);
    }

    // TODO: 2016-02-29 implement email confirmation method in registration controller
    private void sendConfirmationEmail(String email) {

    }

}
