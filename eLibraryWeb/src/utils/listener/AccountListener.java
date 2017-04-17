package utils.listener;

import entities.user.User;
import service.LibraryService;
import utils.CookieUtil;
import utils.SessionUtil;

import javax.ejb.EJB;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

public class AccountListener implements ServletRequestListener {
    @EJB
    LibraryService service;

    @Override
    public void requestInitialized(ServletRequestEvent servletRequestEvent) {
        HttpServletRequest request = (HttpServletRequest) servletRequestEvent.getServletRequest();
        User user = SessionUtil.getSessionAccount(request);
        if (user == null) {
            try {
                user = CookieUtil.getCookieAccount(request, service);
                if (user != null) {
                    SessionUtil.setSessionAccount(user, request);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void requestDestroyed(ServletRequestEvent servletRequestEvent) {

    }
}
