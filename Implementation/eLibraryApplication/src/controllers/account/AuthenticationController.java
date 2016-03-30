package controllers.account;

import com.sun.org.apache.xml.internal.utils.URI;
import data.AccountDB;
import models.Account;
import utils.PasswordUtil;
import utils.dataValidation.DataValidationException;
import utils.dataValidation.InternalDataValidationException;

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
        String url;
        String referrer = req.getHeader("referer");
        if (referrer != null) {
            referrer = new URI(referrer).getPath(true, false);
            if (referrer.equals("/account/login.jsp")) {
                referrer = "/index.jsp";
            }
        }

        try {
            if (action == null) {
                resp.sendError(404);
                return;
            } else if (action.equals("login")) {
                url = login(referrer, req, resp);
            } else if (action.equals("signUp")) {
                url = signUp(referrer, req, resp);
            } else {
                resp.sendError(404);
                return;
            }
//            } else if (action.equals("logout")) {
//                logout(req, resp);
//                url = referrer;
//            }
        } catch (Exception e) {
            log(e.getMessage(), e);
            for (Throwable t : e.getSuppressed()) {
                log(t.getMessage(), t);
            }
            resp.sendError(500);
            return;
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
        String url;
        String referrer = req.getHeader("referer");
        if (referrer != null) {
            referrer = new URI(referrer).getPath(true, false);
            if (referrer.equals("/account/login.jsp")) {
                referrer = "/index.jsp";
            }
        }

        if (action == null) {
            resp.sendError(404);
            return;
        } else if (action.equals("logout")) {
            logout(req, resp);
            url = referrer;
        } else {
            resp.sendError(404);
            return;
        }

        if (referrer != null && url.equals(referrer)) {
            resp.sendRedirect(url);
        } else {
            req.getServletContext().getRequestDispatcher(url).forward(req, resp);
        }
    }

    private String login(String referrer, HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, NoSuchAlgorithmException,
            DataValidationException, InternalDataValidationException {
        String emailOrUsername = req.getParameter("emailOrUsername");
        String password = req.getParameter("password");
        String url;

        if ((emailOrUsername != null && !emailOrUsername.trim().equals("")) &&
                (password != null && !password.trim().equals(""))) {
            Account account = AccountDB.selectAccount(emailOrUsername);
            if (account != null) {
                String saltValue = account.getSaltValue();
                String hashedPassword = PasswordUtil.hashPassword(password + saltValue);
                if (hashedPassword.equals(account.getPassword())) {
                    Account.addAccountCookie(account, resp);
                    if (referrer != null && !referrer.trim().equals("")) {
                        url = referrer;
                    } else {
                        url = "/index.jsp";
                    }
                } else {
                    req.setAttribute("errorMessage", "Invalid username or password");
                    url = "/account/login.jsp";
                }
            } else {
                req.setAttribute("errorMessage", "Invalid username or password");
                url = "/account/login.jsp";
            }
        } else {
            req.setAttribute("errorMessage", "Invalid username or password");
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
