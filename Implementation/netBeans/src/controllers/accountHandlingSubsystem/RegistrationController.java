package controllers.accountHandlingSubsystem;

import data.AccountDB;
import models.Account;
import models.User;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Created by Leonti on 2016-02-27.
 */

@WebServlet(name = "accountRegistration", urlPatterns = "/account/registration")
public class RegistrationController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String finalUrl;

        Account account = initializeAccount(req.getParameterMap());

        if (checkRegistrationData(account, req, resp)) {
            if (!accountExists(account, req, resp)) {
                createAccount(account);
                finalUrl = "/account/confirmRegistration.jsp";
                sendConfirmationEmail(account.getEmail());
            } else {
                finalUrl = "/account/registration.jsp";
            }
        } else {
            finalUrl = "/account/registration.jsp";
        }

        req.setAttribute("account", account);

        getServletContext().getRequestDispatcher(finalUrl).forward(req, resp);
    }

    private Account initializeAccount(Map<String, String[]> parameters) {
        Account account = new Account();

        account.setLogin(parameters.get("login")[0]);
        account.setEmail(parameters.get("email")[0]);
        account.setPassword(parameters.get("password")[0]);
        account.setUser(
                new User(parameters.get("firstName")[0],
                        parameters.get("lastName")[0]));

        return account;
    }

    private boolean checkRegistrationData(
            Account account, HttpServletRequest req, HttpServletResponse resp) {
        boolean flag = true;

        String firstName = account.getUser().getFirstName();
        String lastName = account.getUser().getLastName();
        String email = account.getEmail();
        String login = account.getLogin();
        String password = account.getPassword();
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
        if (login == null || login.equals("")) {
            flag = false;
            req.setAttribute("invalidLogin", true);
            req.setAttribute("invalidLoginMessage", "Please, fill in this field.");
        }
        if (password == null || password.equals("") ||
                password.length() < 6) {
            flag = false;
            req.setAttribute("invalidPassword", true);
            req.setAttribute("invalidPasswordMessage", "Password must be at least 6 characters.");
        }
        // TODO: 2016-02-29 review account password validation
        else if (confirmPassword == null || !password.equals(confirmPassword)) {
            flag = false;
            req.setAttribute("invalidConfirmPassword", true);
            req.setAttribute("invalidConfirmPasswordMessage", "Confirmation password must match password.");
        }

        if (!flag) {
            req.setAttribute("message", "Please, fill in this field.");
        }

        return flag;
    }

    private boolean accountExists(
            Account account, HttpServletRequest req, HttpServletResponse resp) {
        boolean flag = false;

        String login = account.getLogin();
        String email = account.getEmail();

        if (AccountDB.emailExists(email)) {
            flag = true;
            req.setAttribute("invalidEmail", true);
            req.setAttribute("invalidEmailMessage", "The email is already registered.");
        }
        if (AccountDB.loginExists(login)) {
            flag = true;
            req.setAttribute("invalidLogin", true);
            req.setAttribute("invalidLoginMessage", "The login is already used.");
        }

        return flag;
    }

    private void createAccount(Account account) {
        AccountDB.insertAccount(account);
    }

    // TODO: 2016-02-29 implement email confirmation method in registration controller
    private void sendConfirmationEmail(String email) {

    }

    private boolean isValidEmail(String email) {
        boolean flag = true;

        try {
            InternetAddress emailAddress = new InternetAddress(email);
            emailAddress.validate();
        } catch (AddressException e) {
            flag = false;
            log("Email validation", e);
        }

        return flag;
    }
}
