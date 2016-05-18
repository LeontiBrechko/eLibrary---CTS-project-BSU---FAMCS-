package controllers.account;

import com.sun.org.apache.xml.internal.utils.URI;
import data.AccountDB;
import models.Account;
import models.enums.AccountState;
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

        try {
            if (action == null) {
                resp.sendError(404);
                return;
            } else if (action.equals("login")) {
                url = login(req, resp);
            } else if (action.equals("signUp")) {
                url = signUp(req, resp);
            } else {
                resp.sendError(404);
                return;
            }
        } catch (Exception e) {
            log(e.getMessage(), e);
            for (Throwable t : e.getSuppressed()) {
                log(t.getMessage(), t);
            }
            resp.sendError(500);
            return;
        }

        if (url.equals("/account/login.jsp")) {
            req.getServletContext().getRequestDispatcher(url).forward(req, resp);
        } else {
            resp.sendRedirect(url);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        String url;

        if (action == null) {
            resp.sendError(404);
            return;
        } else if (action.equals("logout")) {
            logout(req, resp);
            url = "/index.jsp";
        } else {
            resp.sendError(404);
            return;
        }

        if (url.equals("/account/login.jsp")) {
            req.getServletContext().getRequestDispatcher(url).forward(req, resp);
        } else {
            resp.sendRedirect(url);
        }
    }

    private String login(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, NoSuchAlgorithmException,
            DataValidationException, InternalDataValidationException {
        String emailOrUsername = req.getParameter("emailOrUsername");
        String password = req.getParameter("password");
        String url;

        if ((emailOrUsername != null && !emailOrUsername.trim().equals("")) &&
                (password != null && !password.trim().equals(""))) {
            Account account = AccountDB.selectAccount(emailOrUsername);
            if (account != null) {
                if (account.getState() == AccountState.ACTIVE) {
                    String saltValue = account.getSaltValue();
                    String hashedPassword = PasswordUtil.hashPassword(password + saltValue);
                    if (hashedPassword.equals(account.getPassword())) {
                        Account.addAccountCookie(account, resp);
                        url = "/index.jsp";
                    } else {
                        req.setAttribute("errorMessage", "Invalid username or password");
                        url = "/account/login.jsp";
                    }
                } else {
                    req.setAttribute("errorMessage", "Your account either blocked or not activated.");
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

    private String signUp(HttpServletRequest req, HttpServletResponse resp) {
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
