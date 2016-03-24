package controllers.account;

import com.sun.org.apache.xml.internal.utils.URI;
import data.AccountDB;
import models.Account;
import utils.PasswordUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

/**
 * Created by Leonti on 2016-03-08.
 */

@WebServlet(name = "login", urlPatterns = "/account/login")
public class AuthenticationController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        String referrer = new URI(req.getHeader("referer")).getPath(true, false);
        String url = "/index.jsp";

        if (referrer.equals("/account/login.jsp")) {
            referrer = "/index.jsp";
        }

        try {
            if (action == null || action.equals("")) {
                    // TODO: 2016-03-11 error message
                    url = "/index.jsp";
            } else if (action.equals("login")) {
                url = login(referrer, req, resp);
            } else if (action.equals("signUp")) {
                url = signUp(referrer, req, resp);
            } else if (action.equals("logout")) {
                logout(req, resp);
                url = referrer;
            }
        } catch (SQLException | NoSuchAlgorithmException e) {
            // TODO: 2016-03-11 error message
            e.printStackTrace();
            url = "/index.jsp";
        }

        if (url != null && url.equals(referrer)) {
            resp.sendRedirect(url);
        } else {
            req.getServletContext().getRequestDispatcher(url).forward(req, resp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        String referrer = new URI(req.getHeader("referer")).getPath(true, false);
        String url = "/index.jsp";

        if (referrer.equals("/account/login.jsp")) {
            referrer = "/index.jsp";
        }

        if (action == null || action.equals("")) {
            // TODO: 2016-03-11 error message
            url = "/index.jsp";
        } else if (action.equals("logout")) {
            logout(req, resp);
            url = referrer;
        }

        if (url != null && url.equals(referrer)) {
            resp.sendRedirect(url);
        } else {
            req.getServletContext().getRequestDispatcher(url).forward(req, resp);
        }
    }

    private String login(String referrer, HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, NoSuchAlgorithmException{
        String emailOrUsername = req.getParameter("emailOrUsername");
        String password = req.getParameter("password");
        String url;

        if ((emailOrUsername != null && !emailOrUsername.equals("")) &&
                (password != null && !password.equals(""))) {
            Account account = AccountDB.selectAccount(emailOrUsername);
            if (account != null) {
                String saltValue = account.getSaltValue();
                String hashedPassword = PasswordUtil.hashPassword(password + saltValue);
                if (hashedPassword.equals(account.getPassword())) {
                    Account.addAccountCookie(account, resp);
                    if (referrer != null && !referrer.equals("")) {
                        url = referrer;
                    } else {
                        url = "/index.jsp";
                    }
                } else {
                    // TODO: 2016-03-11 error message
                    url = "/account/login.jsp";
                }
            } else {
                // TODO: 2016-03-11 error message
                url = "/account/login.jsp";
            }
        } else {
            // TODO: 2016-03-11 error message
            url = "/account/login.jsp";
        }

        return url;
    }

    private String signUp(String referrer, HttpServletRequest req, HttpServletResponse resp) {
        req.setAttribute("referrer", referrer);
        return "/account/register.jsp";
    }

    private void logout(HttpServletRequest req, HttpServletResponse resp) {
        Account account = Account.getSessionAccount(req);
        if (account != null) {
            Account.deleteAccountCookie(resp);
            Account.deleteSessionAccount(req);
        }
    }
}
