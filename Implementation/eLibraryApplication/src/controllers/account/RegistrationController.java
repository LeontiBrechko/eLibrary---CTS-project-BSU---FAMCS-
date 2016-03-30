package controllers.account;

import data.UserDB;
import models.Account;
import models.User;
import utils.MailUtil;
import utils.PasswordUtil;
import utils.dataValidation.DataValidationException;
import utils.dataValidation.DataValidationUtil;
import utils.dataValidation.InternalDataValidationException;

import javax.mail.MessagingException;
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
//        String referrer = req.getParameter("referrer");
        String url;

        try {
            setInputData(req);
            User user = initializeUserAccount(req.getParameterMap());
            DataValidationUtil.validateRegistrationData(user.getAccount(), req, resp);

            String saltedAndHashedPassword =
                    PasswordUtil.hashPassword(user.getAccount().getPassword() + user.getAccount().getSaltValue());
            user.getAccount().setPassword(saltedAndHashedPassword);

            UserDB.insertUser(user);

            Account.addAccountCookie(user.getAccount(), resp);
            // TODO: 2016-03-29 confirmation
//            sendConfirmationEmail(referrer, user.getAccount());
            url = "/account/confirm.jsp";

        } catch (DataValidationException e) {
            req.setAttribute("errorMessage", e.getMessage());
            url = "/account/register.jsp";
        } catch (Exception e) {
            log(e.getMessage(), e);
            for (Throwable t : e.getSuppressed()) {
                log(t.getMessage(), t);
            }
            resp.sendError(500);
            return;
        }

        getServletContext().getRequestDispatcher(url).forward(req, resp);
    }

    private void setInputData(HttpServletRequest req) {
        Map<String, String[]> parameters = req.getParameterMap();
        for (String key : parameters.keySet()) {
            req.setAttribute(key, parameters.get(key)[0]);
        }
    }

    private User initializeUserAccount(Map<String, String[]> parameters)
            throws DataValidationException, InternalDataValidationException {
        Account account =
                new Account(parameters.get("username")[0], parameters.get("email")[0],
                        parameters.get("password")[0], PasswordUtil.getSalt(),
                        PasswordUtil.getConfirmationToken());
        User user =
                new User(parameters.get("firstName")[0],
                        parameters.get("lastName")[0],
                        account);

        return user;
    }

    // TODO: 2016-02-29 implement email confirmation method in registration controller
    public static void sendConfirmationEmail(String referrer, Account account)
            throws MessagingException {
        // TODO: 2016-03-11 implement forwarding accordingly to referrer
        MailUtil.sendMail(account.getEmail(), "", "", "", true);
    }
}
