package controllers.accountHandlingSubsystem;

import models.Account;
import models.User;
import models.enums.AccountRole;
import models.enums.AccountState;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Created by Leonti on 2016-02-27.
 */

@WebServlet(name = "registration", urlPatterns = "/account/registration")
public class RegistrationController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String finalUrl;

        if (checkRegistrationData(req, resp)) {
            finalUrl = "/account/confirmRegistration.jsp";
        } else {
            finalUrl = "/account/registration.jsp";
        }

        getServletContext().getRequestDispatcher(finalUrl).forward(req, resp);
    }

    private boolean checkRegistrationData(HttpServletRequest req, HttpServletResponse resp) {
        boolean flag = true;

        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");
        String email = req.getParameter("email");
        String login = req.getParameter("login");
        String password = req.getParameter("password");
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
        }
        if (login == null || login.equals("")) {
            flag = false;
            req.setAttribute("invalidLogin", true);
        }
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

        Account account =
                new Account(login, email, password,
                        AccountRole.USER, LocalDateTime.now(),
                        AccountState.TEPMORARY, new User(firstName, lastName));

        req.setAttribute("account", account);

        return flag;
    }
//
//    private boolean checkAccount() {
//
//    }
//
//    private boolean createAccount() {
//
//    }

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
