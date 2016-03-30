package controllers.catalog;

import data.BookDB;
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
            if (action == null) {
                resp.sendError(404);
                return;
            } else if (action.equals("showDescription")) {
                url = showDescription(req, resp);
            } else if (action.equals("openBook")) {
                url = openBook(req, resp);
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

        getServletContext().getRequestDispatcher(url).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        try {
            if (action == null) {
                resp.sendError(404);
                return;
            } else if (action.equals("showDescription") ||
                    action.equals("openBook")) {
                this.doGet(req, resp);
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
    }

    private String showDescription(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, IOException,
            DataValidationException, InternalDataValidationException {
        String url;
        String isbn13 = req.getParameter("isbn13");

        if (isbn13 == null || isbn13.equals("")) {
            // TODO: 2016-03-11 error message
            url = "/index.jsp";
        } else {
            Book book = BookDB.selectBook(isbn13);
            if (book != null) {
                if (!book.getDescription().trim().equals("")) {
                    req.setAttribute("bookDescription",
                            book.readDescriptionFile(
                                    req.getServletContext().getRealPath(book.getDescription())));
                }
                req.setAttribute("book", book);
                url = "/catalog/description.jsp";
            } else {
                // TODO: 2016-03-11 error message
                url = "/index.jsp";
            }
        }

        return url;
    }

    private String openBook(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException,
            DataValidationException, InternalDataValidationException {
        String url = "";
        String isbn13 = req.getParameter("isbn13");

        if (isbn13 != null && !isbn13.equals("")) {
            Book book = BookDB.selectBook(isbn13);
            if (book != null) {
                for (BookFile file : book.getFiles()) {
                    if (file.getFormat() == Format.PDF) {
                        String path = getServletContext().getRealPath(file.getPath());
                        File pdfFile = new File(path).getAbsoluteFile();
                        Files.copy(pdfFile.toPath(), resp.getOutputStream());
                        resp.setHeader("Content-Type", getServletContext().getMimeType(pdfFile.getName()));
                        resp.setHeader("Content-Length", String.valueOf(pdfFile.length()));
                        resp.setHeader("Content-Disposition", "inline; filename=\"" + pdfFile.getName() + "\"");
                        url = "";
                        break;
                    } else {
                        // TODO: 2016-03-11 error message
                        url = "/catalog/description?action=showDescription&amp;isbn13=" + isbn13;
                    }
                }
            } else {
                // TODO: 2016-03-11 error message
                url = "/index.jsp";
            }
        } else {
            // TODO: 2016-03-11 error message
            url = "/index.jsp";
        }

        return url;
    }
}
