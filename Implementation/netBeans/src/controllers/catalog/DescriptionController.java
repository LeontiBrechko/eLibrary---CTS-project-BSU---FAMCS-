package controllers.catalog;

import data.AccountDB;
import data.BookDB;
import models.Account;
import models.Book;
import models.BookFile;
import models.enums.Format;

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
        String url = "";

        try {
            if (action == null) {
                // TODO: 2016-03-11 error message
                resp.sendRedirect(url = "/index.jsp");
            } else if (action.equals("showDescription")) {
                url = showDescription(req, resp);
            } else if (action.equals("openBook")) {
                url = openBook(req, resp);
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            // TODO: 2016-03-11 error message
            url = "/index.jsp";
        }

        if (url != null && !url.equals("")) {
            if (url.equals("/index.jsp")) {
                resp.sendRedirect(url);
            } else {
                getServletContext().getRequestDispatcher(url).forward(req, resp);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if (action == null) {

        } else if (action.equals("showDescription") ||
                action.equals("openBook") ||
                action.equals("addToDownloadList")) {
            this.doGet(req, resp);
        }
    }

    private String showDescription(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, IOException {
        String url;
        String isbn13 = req.getParameter("isbn13");

        if (isbn13 == null || isbn13.equals("")) {
            // TODO: 2016-03-11 error message
            url = "/index.jsp";
        } else {
            Book book = BookDB.selectBook(isbn13);
            if (book != null) {
                if (book.getDescription() != null && !book.getDescription().equals("")) {
                    book.setDescription(req.getServletContext().getRealPath(book.getDescription()));
                    book.readDescription();
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
            throws IOException, SQLException {
        String url = "";
        String isbn13 = req.getParameter("isbn13");

        if (isbn13 != null && !isbn13.equals("")) {
            Book book = BookDB.selectBook(isbn13);
            if (book != null) {
                book.setPopularity(book.getPopularity() + 1);
                BookDB.updateBook(book);
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
