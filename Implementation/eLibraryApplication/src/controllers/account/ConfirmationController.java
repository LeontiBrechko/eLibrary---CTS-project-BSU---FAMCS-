package controllers.account;

import data.AccountDB;
import models.Account;
import models.enums.AccountState;
import utils.MailUtil;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Leonti on 2016-02-29.
 */

@WebServlet(name = "accountConfirmation", urlPatterns = "/account/confirmation")
public class ConfirmationController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // TODO: 2016-02-29 insertAccount email parameter in request
        String email = req.getParameter("email");

//        Account account = getAccount(email);

        // TODO: 2016-02-29 implement catch for nullPointerException
//        account.setState(AccountState.ACTIVE);
//        changeAccount(account);
    }


//    private Account getAccount(String email) {
//        return AccountDB.selectAccount(email);
//    }

//    private void changeAccount(Account account) {
//        AccountDB.updateAccount(account);
//    }
}
