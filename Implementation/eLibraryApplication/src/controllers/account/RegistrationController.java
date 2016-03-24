package controllers.account;

import data.AccountDB;
import data.UserDB;
import models.Account;
import models.User;
import utils.MailUtil;
import utils.PasswordUtil;
import utils.RegistrationUtil;

import javax.mail.MessagingException;
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
            if (RegistrationUtil.checkRegistrationData(user, req, resp) &
                    !RegistrationUtil.accountExists(user.getAccount(), req, resp)) {

                RegistrationUtil.createUserAccount(user);
                Account.addAccountCookie(user.getAccount(), resp);
                RegistrationUtil.sendConfirmationEmail(referrer, user.getAccount());
                url = "/account/confirm.jsp";
            } else {
                // TODO: 2016-03-11 error message
                url = "/account/register.jsp";
                req.setAttribute("user", user);
            }
        } catch (SQLException | NoSuchAlgorithmException | MessagingException e) {
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
}
