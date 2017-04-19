package controller.admin;

import entities.user.Role;
import entities.user.User;
import entities.user.UserRole;
import entities.user.UserState;
import service.LibraryService;
import util.CookieUtil;
import util.PasswordUtil;
import util.validation.DataValidationException;
import util.validation.DataValidationUtil;
import util.validation.InternalDataValidationException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "accountManagement", urlPatterns = "/admin/accountManagement")
public class AccountManagementController extends HttpServlet {
    @EJB
    LibraryService service;

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
                User admin = new User(req.getParameter("firstName"), req.getParameter("lastName"),
                        req.getParameter("username"), req.getParameter("email"),
                        req.getParameter("password"), PasswordUtil.getConfirmationToken(), PasswordUtil.getSalt());
                admin.getRoles().add(new UserRole(Role.ADMIN.name()));
                admin.setState(UserState.ACTIVE);

                DataValidationUtil.validateRegistrationData(admin, req, resp, service);

                String saltedAndHashedPassword =
                        PasswordUtil.hashPassword(admin.getPassword() + admin.getSaltValue());
                admin.setPassword(saltedAndHashedPassword);

                service.saveUser(admin);

                CookieUtil.addAccountCookie(admin, resp);
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
        List<User> users = service.findAllUsers();
        req.setAttribute("accounts", users);

        return "/admin/accounts/libraryAccounts.jsp";
    }

    private String blockAccount(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, DataValidationException, InternalDataValidationException  {
        String url;
        String username = req.getParameter("username");

        if (username != null && !username.equals("")) {
            User user = service.findUserByUsername(username);
            user.setState(UserState.FROZEN);
            service.saveUser(user);

            showAllAccounts(req, resp);

            url = "/admin/accounts/libraryAccounts.jsp";
        } else {
            url = "/catalog/catalog.jsp";
        }

        return url;
    }

    private String unblockAccount(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, DataValidationException, InternalDataValidationException  {
        String url;
        String username = req.getParameter("username");

        if (username != null && !username.equals("")) {
            User user = service.findUserByUsername(username);
            user.setState(UserState.ACTIVE);
            service.saveUser(user);

            showAllAccounts(req, resp);

            url = "/admin/accounts/libraryAccounts.jsp";
        } else {
            url = "/catalog/catalog.jsp";
        }

        return url;
    }

    private void setInputData(HttpServletRequest req) {
        Map<String, String[]> parameters = req.getParameterMap();
        for (String key : parameters.keySet()) {
            req.setAttribute(key, parameters.get(key)[0]);
        }
    }
}
