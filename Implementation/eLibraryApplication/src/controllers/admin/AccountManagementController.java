package controllers.admin;

import data.AccountDB;
import data.UserDB;
import models.Account;
import models.User;
import models.enums.AccountRole;
import models.enums.AccountState;
import utils.PasswordUtil;
import utils.dataValidation.DataValidationException;
import utils.dataValidation.DataValidationUtil;
import utils.dataValidation.InternalDataValidationException;

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
        String url;
        String action = req.getParameter("action");

        try {
            if (action == null) {
                resp.sendError(404);
                return;
            } else if (action.equals("showAccounts")) {
                url = showAllAccounts(req, resp);
            } else if (action.equals("block")) {
                url = blockAccount(req, resp);
            } else if (action.equals("unblock")) {
                url = unblockAccount(req, resp);
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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url;
        String action = req.getParameter("action");

        try {
            if (action == null) {
                resp.sendError(404);
                return;
            } else if (action.equals("registerAdmin")) {
                setInputData(req);
                User admin = initializeAdminAccount(req.getParameterMap());
                DataValidationUtil.validateRegistrationData(admin.getAccount(), req, resp);

                String saltedAndHashedPassword =
                        PasswordUtil.hashPassword(admin.getAccount().getPassword() + admin.getAccount().getSaltValue());
                admin.getAccount().setPassword(saltedAndHashedPassword);

                UserDB.insertUser(admin);

                Account.addAccountCookie(admin.getAccount(), resp);
                url = "/account/confirm.jsp";
                req.setAttribute("user", admin);
            } else {
                resp.sendError(404);
                return;
            }
        } catch (DataValidationException e) {
            req.setAttribute("errorMessage", e.getMessage());
            url = "/admin/accounts/adminRegistration.jsp";
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

    private String showAllAccounts(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, DataValidationException, InternalDataValidationException {
        List<Account> accounts = AccountDB.selectAccountList();
        req.setAttribute("accounts", accounts);

        return "/admin/accounts/libraryAccounts.jsp";
    }

    private String blockAccount(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, DataValidationException, InternalDataValidationException  {
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
            throws SQLException, DataValidationException, InternalDataValidationException  {
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

    private void setInputData(HttpServletRequest req) {
        Map<String, String[]> parameters = req.getParameterMap();
        for (String key : parameters.keySet()) {
            req.setAttribute(key, parameters.get(key)[0]);
        }
    }

    private User initializeAdminAccount(Map<String, String[]> parameters)
            throws DataValidationException, InternalDataValidationException {

        Account account =
                new Account(parameters.get("username")[0], parameters.get("email")[0],
                        parameters.get("password")[0], PasswordUtil.getSalt(),
                        PasswordUtil.getConfirmationToken());
        User user =
                new User(parameters.get("firstName")[0],
                        parameters.get("lastName")[0],
                        account);

        user.getAccount().setRole(AccountRole.ADMIN);
        user.getAccount().setState(AccountState.ACTIVE);

        return user;
    }
}
