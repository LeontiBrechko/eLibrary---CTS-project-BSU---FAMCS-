package util;

import entities.user.User;
import service.LibraryService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

public class CookieUtil {
    public static String getCookieValue(Cookie[] cookies, String cookieName) {
        String cookieValue = "";
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookieName.equals(cookie.getName())) {
                    cookieValue = cookie.getValue();
                    break;
                }
            }
        }
        return cookieValue;
    }

    public static void deleteAllCookies(Cookie[] cookies, HttpServletResponse resp) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                cookie.setMaxAge(0);
                cookie.setPath("/");
                resp.addCookie(cookie);
            }
        }
    }

    public static User getCookieAccount(HttpServletRequest req, LibraryService service)
            throws SQLException {
        Cookie[] cookies = req.getCookies();
        String username = CookieUtil.getCookieValue(cookies, "username");

        User user = null;

        if (username != null && !username.trim().equals("")) {
            user = service.findUserByUsername(username);
        }

        return user;
    }

    public static void addAccountCookie(User user, HttpServletResponse resp) {
        Cookie cookie = new Cookie("username", user.getUsername());
        cookie.setMaxAge(60 * 60 * 24 * 365 * 2);
        cookie.setPath("/");

        resp.addCookie(cookie);
    }

    public static void deleteAccountCookie(HttpServletResponse resp) {
        Cookie cookie = new Cookie("username", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");

        resp.addCookie(cookie);
    }
}
