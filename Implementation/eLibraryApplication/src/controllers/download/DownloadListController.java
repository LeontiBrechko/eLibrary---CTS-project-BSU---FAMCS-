package controllers.download;

import data.AccountDB;
import data.BookDB;
import models.Account;
import models.Book;
import utils.MailUtil;
import utils.ZipUtil;
import utils.dataValidation.DataValidationException;
import utils.dataValidation.InternalDataValidationException;

import javax.mail.MessagingException;
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
        String url;
        String action = req.getParameter("action");

        try {
            if (action == null) {
                resp.sendError(404);
                return;
            } else if (action.equals("showDownloadList")) {
                url = showDownloadList(req, resp);
            } else if (action.equals("addToDownloadList")) {
                url = addToDownloadList(req, resp);
            } else if (action.equals("deleteFromDownloadList")) {
                url = deleteFromDownloadList(req, resp);
            } else if (action.equals("download")) {
                url = downloadBooks(req, resp);
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

        if (url.trim().equals("")) {
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
            throws SQLException, IOException,
            DataValidationException, InternalDataValidationException {
        String url;
        String isbn13 = req.getParameter("isbn13");

        Account account = Account.getSessionAccount(req);
        List<Book> downloadList = account.getDownloadList();
        Book book = BookDB.selectBook(isbn13);

        if (book != null) {
            if (account.listContainsBook(book.getIsbn13()) == -1) {
                downloadList.add(book);
                if (!book.getDescription().trim().equals("")) {
                    req.setAttribute("bookDescription",
                            book.readDescriptionFile(
                                    req.getServletContext().getRealPath(book.getDescription())));
                }
                AccountDB.updateAccount(account);
            }
            req.setAttribute("book", book);
            url = "/catalog/description?action=showDescription&amp;isbn13=" + isbn13;
        } else {
            throw new InternalDataValidationException("Incorrect book isbn13");
        }

        return url;
    }

    private String deleteFromDownloadList(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, DataValidationException {
        Account account = Account.getSessionAccount(req);
        account.getDownloadList().remove(account.listContainsBook(req.getParameter("isbn13")));
        AccountDB.updateAccount(account);

        return "";
    }

    private String downloadBooks(HttpServletRequest req, HttpServletResponse resp) {
        new Thread() {
            // TODO: 2016-05-18 make proper exception handling
            @Override
            public void run() {
                Account account = Account.getSessionAccount(req);
                String zipFile;
                try {
                    zipFile = ZipUtil.createDownloadsZip(account, req.getServletContext());
                    account.getDownloadList().clear();
                    MailUtil.sendDownloadZip(account.getEmail(),
                            account.getUsername(),
                            "elibraryprojectfamcs@gmail.com",
                            "Book download", zipFile);
                } catch (IOException | InternalDataValidationException
                        | SQLException | MessagingException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        return "/download/downloadConfirm.jsp";
    }
}
