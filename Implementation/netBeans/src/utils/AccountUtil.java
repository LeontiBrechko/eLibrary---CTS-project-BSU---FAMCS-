package utils;

import data.AccountDB;
import models.Account;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;

/**
 * Created by Leonti on 2016-03-10.
 */
public class AccountUtil {
    public static boolean sessionAccountExists(Account account,
                                             HttpServletRequest req,
                                             HttpServletResponse resp) {
        boolean flag;
        HttpSession session = req.getSession();

        if (account == null) {
            Cookie[] cookies = req.getCookies();
            String username = CookieUtil.getCookieValue(cookies, "username");

            if (username == null || username.equals("")) {
                flag = false;
            } else {
                try {
                    account = AccountDB.selectAccount(username);
                    session.setAttribute("account", account);
                    flag = true;
                } catch (SQLException e) {
                    e.printStackTrace();
                    flag = false;
                }
            }
        } else {
            flag = true;
        }

        return flag;
    }
}
