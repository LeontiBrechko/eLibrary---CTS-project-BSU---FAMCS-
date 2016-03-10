package controllers.account;

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
public class LoggingController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String emailOrUsername = req.getParameter("emailOrUsername");
        String password = req.getParameter("password");
        String url;

        if (emailOrUsername != null && !emailOrUsername.equals("") &&
                password != null && !password.equals("")) {
            emailOrUsername = emailOrUsername.toLowerCase();
            try {
                Account account = AccountDB.selectAccount(emailOrUsername);
                if (account != null) {
                    String saltValue = account.getSaltValue();
                    try {
                        String hashedPassword = PasswordUtil.hashPassword(password + saltValue);
                        if (hashedPassword.equals(account.getPassword())) {
                            String referrer = req.getHeader("referer");
                            if (referrer != null) {
                                int start = referrer.indexOf("/") + 2;
                                url = referrer.substring(start, referrer.length());
                            } else {
                                url = "/index.jsp";
                            }
                        } else {
                            url = "/account/login.jsp";
                        }
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                        url = "/account/login.jsp";
                    }
                } else {
                    url = "/account/login.jsp";
                }
            } catch (SQLException e) {
                e.printStackTrace();
                url = "/account/login.jsp";
            }
        } else {
            url = "/account/login.jsp";
        }

        req.getServletContext().getRequestDispatcher(url).forward(req, resp);
//        resp.sendRedirect(url);
    }
}
