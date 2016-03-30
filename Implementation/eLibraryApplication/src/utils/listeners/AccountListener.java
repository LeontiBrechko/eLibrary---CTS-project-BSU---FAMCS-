package utils.listeners;

import models.Account;
import utils.dataValidation.DataValidationException;
import utils.dataValidation.InternalDataValidationException;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

/**
 * Created by Leonti on 2016-03-15.
 */
public class AccountListener implements ServletRequestListener {
    @Override
    public void requestInitialized(ServletRequestEvent servletRequestEvent) {
        HttpServletRequest request = (HttpServletRequest) servletRequestEvent.getServletRequest();
        Account account = Account.getSessionAccount(request);
        if (account == null) {
            try {
                account = Account.getCookieAccount(request);
                if (account != null) {
                    Account.setSessionAccount(account, request);
                }
            } catch (SQLException | DataValidationException | InternalDataValidationException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void requestDestroyed(ServletRequestEvent servletRequestEvent) {

    }
}
