package controllers.account;

import data.AccountDB;
import models.Account;
import models.enums.AccountState;
import utils.MailUtil;
import utils.dataValidation.DataValidationException;
import utils.dataValidation.InternalDataValidationException;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by Leonti on 2016-02-29.
 */

@WebServlet(name = "accountConfirmation", urlPatterns = "/account/confirmation")
public class ConfirmationController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = "/account/confirm.jsp";

        try {
            Account account = AccountDB.selectAccount(req.getParameter("username"));
            if (account != null) {
                String token = req.getParameter("token");
                if (account.getConfirmationToken().equals(token)) {
                    if (account.getState() == AccountState.TEMPORARY) {
                        account.setState(AccountState.ACTIVE);
                        AccountDB.updateAccount(account);
                    } else {
                        req.setAttribute("errorMessage", "Your account is already confirmed.");
                        url = "/index.jsp";
                    }
                } else {
                    req.setAttribute("errorMessage", "Invalid confirmation token.");
                    url = "/index.jsp";
                }
            } else {
                req.setAttribute("errorMessage", "No such user in the system.");
                url = "/index.jsp";
            }
        } catch (DataValidationException e) {
            req.setAttribute("errorMessage", e.getMessage());
            url = "/index.jsp";
        } catch (Exception e) {
            log(e.getMessage(), e);
            for (Throwable t : e.getSuppressed()) {
                log(t.getMessage(), t);
            }
            resp.sendError(500);
            return;
        }

        req.getRequestDispatcher(url).forward(req, resp);
    }


//    private Account getAccount(String email) {
//        return AccountDB.selectAccount(email);
//    }

//    private void changeAccount(Account account) {
//        AccountDB.updateAccount(account);
//    }
}
