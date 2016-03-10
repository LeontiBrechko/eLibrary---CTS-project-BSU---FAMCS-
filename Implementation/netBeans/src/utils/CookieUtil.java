package utils;

import javax.servlet.http.Cookie;

/**
 * Created by Leonti on 2016-03-10.
 */
public class CookieUtil {
    public static String getCookieValue(Cookie[] cookies, String cookieName) {
        String cookieValue = "";
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookieName.equals(cookie.getName())){
                    cookieValue = cookie.getValue();
                    break;
                }
            }
        }
        return cookieValue;
    }
}
