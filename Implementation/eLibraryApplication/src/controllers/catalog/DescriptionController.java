package controllers.catalog;

import data.BookDB;
import models.Account;
import models.Book;
import models.BookFile;
import models.enums.Format;
import utils.dataValidation.DataValidationException;
import utils.dataValidation.InternalDataValidationException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;

/**
 * Created by Leonti on 2016-03-03.
 */
@WebServlet(name = "bookDescription", urlPatterns = "/catalog/description")
public class DescriptionController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        String url;

        try {
            String isbn13 = req.getParameter("isbn13");
            Book book;
            if (isbn13 != null) {
                book = BookDB.selectBook(isbn13);
                if (book == null) {
                    resp.sendError(404);
                    return;
                }
            } else {
                resp.sendError(404);
                return;
            }

            if (action == null) {
                resp.sendError(404);
                return;
            } else if (action.equals("showDescription")) {
                url = showDescription(book, req, resp);
            } else if (action.equals("openBook")) {
                url = openBook(book, req, resp);
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

        if (!url.equals("openedBook")) {
            getServletContext().getRequestDispatcher(url).forward(req, resp);
        }
    }

    private String showDescription(Book book, HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, IOException,
            DataValidationException, InternalDataValidationException {
        req.setAttribute("bookDescription",
                book.readDescriptionFile(
                        req.getServletContext().getRealPath(book.getDescription())));

        Account account = Account.getSessionAccount(req);
        boolean isAddable = true;
        if (account != null && account.listContainsBook(book.getIsbn13()) != -1) {
            isAddable = false;
        }

        boolean isOpenable = false;
        for (BookFile file : book.getFiles()) {
            if (file.getFormat() == Format.PDF) {
                isOpenable = true;
                break;
            }
        }

        req.setAttribute("isOpenable", isOpenable);
        req.setAttribute("isAddable", isAddable);
        req.setAttribute("book", book);
        return "/catalog/description.jsp";
    }

    private String openBook(Book book, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException,
            DataValidationException, InternalDataValidationException {
        String url = "/catalog/description?action=showDescription&amp;isbn13=" + book.getIsbn13();

        for (BookFile file : book.getFiles()) {
            if (file.getFormat() == Format.PDF) {
                String path = getServletContext().getRealPath(file.getPath());
                File pdfFile = new File(path).getAbsoluteFile();
                Files.copy(pdfFile.toPath(), resp.getOutputStream());
                resp.setHeader("Content-Type", getServletContext().getMimeType(pdfFile.getName()));
                resp.setHeader("Content-Length", String.valueOf(pdfFile.length()));
                resp.setHeader("Content-Disposition", "inline; filename=\"" + pdfFile.getName() + "\"");
                url = "openedBook";
                break;
            }
        }

        return url;
    }
}
