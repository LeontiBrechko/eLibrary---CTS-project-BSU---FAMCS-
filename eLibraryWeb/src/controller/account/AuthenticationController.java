package controller.account;

import entities.user.User;
import entities.user.UserState;
import service.LibraryService;
import util.CookieUtil;
import util.PasswordUtil;
import util.SessionUtil;
import util.validation.DataValidationException;
import util.validation.InternalDataValidationException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

@WebServlet(name = "login", urlPatterns = "/account/login")
public class AuthenticationController extends HttpServlet {
    @EJB
    LibraryService service;

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

        req.getServletContext().getRequestDispatcher(url).forward(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        String url;

        if (action == null) {
            url = "/account/login.jsp";
        } else if (action.equals("logout")) {
            logout(req, resp);
            url = "/catalog/catalog.jsp";
        } else {
            resp.sendError(404);
            return;
        }

        req.getServletContext().getRequestDispatcher(url).forward(req, resp);
    }

    private String login(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, NoSuchAlgorithmException,
            DataValidationException, InternalDataValidationException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String url;

        if ((username != null && !username.trim().equals("")) &&
                (password != null && !password.trim().equals(""))) {
            User user = service.findUserByUsername(username);
            if (user != null) {
                if (user.getState() == UserState.ACTIVE) {
                    String saltValue = user.getSaltValue();
                    String hashedPassword = PasswordUtil.hashPassword(password + saltValue);
                    if (hashedPassword.equals(user.getPassword())) {
                        CookieUtil.addAccountCookie(user, resp);
                        url = "/catalog/catalog.jsp";
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
        User user = SessionUtil.getSessionAccount(req);
        if (user != null) {
            CookieUtil.deleteAccountCookie(resp);
            SessionUtil.deleteSessionAccount(req);
        }
    }
}
