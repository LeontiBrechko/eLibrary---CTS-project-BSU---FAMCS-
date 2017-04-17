package util;

import entities.Book;
import entities.user.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionUtil {
    public static User getSessionAccount(HttpServletRequest req) {
        HttpSession session = req.getSession();
        final Object lock = session.getId().intern();

        User account;

        synchronized (lock) {
            account = (User) session.getAttribute("account");
        }

        return account;
    }

    public static void setSessionAccount(User user,
                                         HttpServletRequest req) {
        HttpSession session = req.getSession();
        final Object lock = session.getId().intern();

        synchronized (lock) {
            session.setAttribute("account", user);
        }
    }

    public static void deleteSessionAccount(HttpServletRequest req) {
        HttpSession session = req.getSession();
        final Object lock = session.getId().intern();

        synchronized (lock) {
            session.removeAttribute("account");
        }
    }

    public static Book getBookToUpdate(HttpServletRequest req) {
        HttpSession session = req.getSession();
        final Object lock = session.getId().intern();

        Book book;

        synchronized (lock) {
            book = (Book) session.getAttribute("bookToUpdate");
        }

        return book;
    }

    public static void setBookToUpdate(Book book,
                                       HttpServletRequest req) {
        HttpSession session = req.getSession();
        final Object lock = session.getId().intern();

        synchronized (lock) {
            session.setAttribute("bookToUpdate", book);
        }
    }

    public static void deleteBookToUpdate(HttpServletRequest req) {
        HttpSession session = req.getSession();
        final Object lock = session.getId().intern();

        synchronized (lock) {
            session.removeAttribute("bookToUpdate");
        }
    }
}
