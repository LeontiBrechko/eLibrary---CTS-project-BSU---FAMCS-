package controller.account;

import entities.user.User;
import entities.user.UserState;
import service.LibraryService;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "accountConfirmation", urlPatterns = "/controller/account/confirmation")
public class ConfirmationController extends HttpServlet {
    @EJB
    LibraryService service;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = "/controller/account/confirm.jsp";

        try {
            User user = service.findUserByUsername(req.getParameter("username"));
            if (user != null) {
                String token = req.getParameter("token");
                if (user.getConfirmToken().equals(token)) {
                    if (user.getState() == UserState.TEMPORARY) {
                        user.setState(UserState.ACTIVE);
                        service.saveUser(user);
                    } else {
                        req.setAttribute("errorMessage", "Your controller.account is already confirmed.");
                        url = "/index.jsp";
                    }
                } else {
                    req.setAttribute("errorMessage", "Invalid confirmation token.");
                    url = "/index.jsp";
                }
            } else {
                req.setAttribute("errorMessage", "No such user in the system.");
                url = "/index.jsp";
            }
        } catch (Exception e) {
            log(e.getMessage(), e);
            for (Throwable t : e.getSuppressed()) {
                log(t.getMessage(), t);
            }
            resp.sendError(500);
            return;
        }

        req.getRequestDispatcher(url).forward(req, resp);
    }
}
