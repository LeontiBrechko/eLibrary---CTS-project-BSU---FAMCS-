package controller.account;

import entities.user.User;
import service.LibraryService;
import utils.CookieUtil;
import utils.MailUtil;
import utils.PasswordUtil;
import utils.validation.DataValidationException;
import utils.validation.DataValidationUtil;

import javax.ejb.EJB;
import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

@WebServlet(name = "accountRegistration", urlPatterns = "/controller/account/registration")
public class RegistrationController extends HttpServlet {
    @EJB
    LibraryService service;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url;

        try {
            setInputData(req);
            User user = new User(req.getParameter("firstName"), req.getParameter("lastName"),
                    req.getParameter("username"), req.getParameter("email"),
                    req.getParameter("password"), PasswordUtil.getConfirmationToken(), PasswordUtil.getSalt());
            DataValidationUtil.validateRegistrationData(user, req, resp, service);

            String saltedAndHashedPassword =
                    PasswordUtil.hashPassword(user.getPassword() + user.getSaltValue());
            user.setPassword(saltedAndHashedPassword);

            CookieUtil.addAccountCookie(user, resp);
            sendConfirmationEmail(req, user);
            service.saveUser(user);

            url = "/controller/account/confirm.jsp";

        } catch (DataValidationException e) {
            req.setAttribute("errorMessage", e.getMessage());
            url = "/controller/account/register.jsp";
        } catch (Exception e) {
            log(e.getMessage(), e);
            for (Throwable t : e.getSuppressed()) {
                log(t.getMessage(), t);
            }
            resp.sendError(500);
            return;
        }

        getServletContext().getRequestDispatcher(url).forward(req, resp);
    }

    private void setInputData(HttpServletRequest req) {
        Map<String, String[]> parameters = req.getParameterMap();
        for (String key : parameters.keySet()) {
            req.setAttribute(key, parameters.get(key)[0]);
        }
    }

    private static void sendConfirmationEmail(HttpServletRequest req, User user)
            throws MessagingException, UnsupportedEncodingException {
        String confirmationLink = "http://" + req.getServerName() + ":" + req.getServerPort()
                + "/controller/account/confirmation?username=" + user.getUsername()
                + "&token=" + URLEncoder.encode(user.getConfirmToken(), "UTF-8");
        MailUtil.sendConfirmationEmail(user.getEmail(), user.getUsername(),
                "elibraryprojectfamcs@gmail.com", "Account activation", confirmationLink);
    }
}
