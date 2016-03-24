package controllers.admin;

import data.AccountDB;
import models.Account;
import models.User;
import models.enums.AccountRole;
import models.enums.AccountState;
import utils.RegistrationUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by Leonti on 2016-03-21.
 */

@WebServlet(name = "accountManagement", urlPatterns = "/admin/accountManagement")
public class AccountManagementController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = "/index.jsp";
        String action = req.getParameter("action");

        try {
            if (action == null || action.equals("")) {
                url = "/index.jsp";
            } else if (action.equals("showAccounts")) {
                url = showAllAccounts(req, resp);
            } else if (action.equals("block")) {
                url = blockAccount(req, resp);
            } else if (action.equals("unblock")) {
                url = unblockAccount(req, resp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            url = "/index.jsp";
        }

        if (url.equals("/index.jsp")) {
            resp.sendRedirect(url);
        } else {
            req.getServletContext().getRequestDispatcher(url).forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = "/index.jsp";
        String action = req.getParameter("action");

        try {
            if (action == null || action.equals("")) {
                url = "/index.jsp";
            } else if (action.equals("registerAdmin")) {
                User admin = initializeAdminAccount(req.getParameterMap());

                if (RegistrationUtil.checkRegistrationData(admin, req, resp) &
                        !RegistrationUtil.accountExists(admin.getAccount(), req, resp)) {
                    RegistrationUtil.createUserAccount(admin);
                    Account.addAccountCookie(admin.getAccount(), resp);
                    url = "/account/confirm.jsp";
                } else {
                    // TODO: 2016-03-11 error message
                    url = "/admin/accounts/adminRegistration.jsp";
                    req.setAttribute("user", admin);
                }
            }
        } catch (SQLException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            url = "/index.jsp";
        }

        if (url.equals("/index.jsp")) {
            resp.sendRedirect(url);
        } else {
            req.getServletContext().getRequestDispatcher(url).forward(req, resp);
        }
    }

    private String showAllAccounts(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException {
        List<Account> accounts = AccountDB.selectAccountList();
        req.setAttribute("accounts", accounts);

        return "/admin/accounts/libraryAccounts.jsp";
    }

    private String blockAccount(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException {
        String url;
        String username = req.getParameter("username");

        if (username != null && !username.equals("")) {
            Account account = AccountDB.selectAccount(username);
            account.setState(AccountState.FROZEN);
            AccountDB.updateAccount(account);

            showAllAccounts(req, resp);

            url = "/admin/accounts/libraryAccounts.jsp";
        } else {
            url = "/index.jsp";
        }

        return url;
    }

    private String unblockAccount(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException {
        String url;
        String username = req.getParameter("username");

        if (username != null && !username.equals("")) {
            Account account = AccountDB.selectAccount(username);
            account.setState(AccountState.ACTIVE);
            AccountDB.updateAccount(account);

            showAllAccounts(req, resp);

            url = "/admin/accounts/libraryAccounts.jsp";
        } else {
            url = "/index.jsp";
        }

        return url;
    }

    private User initializeAdminAccount(Map<String, String[]> parameters) {
        User user = new User();

        user.getAccount().setUsername(parameters.get("username")[0]);
        user.getAccount().setEmail(parameters.get("email")[0]);
        user.getAccount().setPassword(parameters.get("password")[0]);
        user.setFirstName(parameters.get("firstName")[0]);
        user.setLastName(parameters.get("lastName")[0]);
        user.getAccount().setRole(AccountRole.ADMIN);
        user.getAccount().setState(AccountState.ACTIVE);

        return user;
    }
}
