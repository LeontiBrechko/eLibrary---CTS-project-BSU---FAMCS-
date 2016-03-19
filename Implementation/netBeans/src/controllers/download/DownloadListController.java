package controllers.download;

import data.AccountDB;
import data.BookDB;
import models.Account;
import models.Book;
import utils.ZipUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Leonti on 2016-03-14.
 */

@WebServlet(name = "downloadList", urlPatterns = "/download/downloadList")
public class DownloadListController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = "/index.jsp";
        String action = req.getParameter("action");

        try {
            if (action == null || action.equals("")) {
                url = "/index.jsp";
            } else if (action.equals("showDownloadList")) {
                // TODO: 2016-03-14 check for logging in
                url = showDownloadList(req, resp);
            } else if (action.equals("addToDownloadList")) {
                url = addToDownloadList(req, resp);
            } else if (action.equals("deleteFromDownloadList")) {
                url = deleteFromDownloadList(req, resp);
            } else if (action.equals("download")) {
                url = downloadBooks(req, resp);
            }
        } catch (SQLException e) {
            // TODO: 2016-03-14 error message
            e.printStackTrace();
            url = "/index.jsp";
        }

        if (url.equals("")) {
            resp.sendRedirect("/download/downloadList.jsp");
        } else {
            req.getServletContext().getRequestDispatcher(url).forward(req, resp);
        }
    }

    private String showDownloadList(HttpServletRequest req, HttpServletResponse resp) {
        String url;

        url = "/download/downloadList.jsp";

        return url;
    }

    private String addToDownloadList(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, IOException {
        String url;
        String isbn13 = req.getParameter("isbn13");

        Account account = Account.getSessionAccount(req);
        List<Book> downloadList = account.getDownloadList();
        Book book = BookDB.selectBook(isbn13);

        if (book != null) {
            if (account.listContainsBook(book) == -1) {
                downloadList.add(book);
                book.setDescription(req.getServletContext().getRealPath(book.getDescription()));
                if (book.getDescription() != null && !book.getDescription().equals("")) {
                    book.readDescription();
                }
                AccountDB.updateAccount(account);
                // TODO: 2016-03-11 success message, change add to list button as read-only
            } else {
                // TODO: 2016-03-11 error message
            }
            req.setAttribute("book", book);
            url = "/catalog/description?action=showDescription&amp;isbn13=" + isbn13;
        } else {
            // TODO: 2016-03-11 error message
            url = "/index.jsp";
        }

        return url;
    }

    private String deleteFromDownloadList(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException {
        Book book = new Book();
        book.setIsbn13(req.getParameter("isbn13"));

        Account account = Account.getSessionAccount(req);
        account.getDownloadList().remove(account.listContainsBook(book));
        AccountDB.updateAccount(account);

        return "";
    }

    private String downloadBooks(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, IOException {
        Account account = Account.getSessionAccount(req);
        ZipUtil.createDownloadsZip(account.getDownloadList(), req.getServletContext());
        return "";
    }
}
