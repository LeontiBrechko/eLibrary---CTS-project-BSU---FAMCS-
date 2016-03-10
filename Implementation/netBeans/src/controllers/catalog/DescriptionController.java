package controllers.catalog;

import data.BookDB;
import models.Account;
import models.Book;
import models.BookFile;
import models.enums.Format;
import utils.AccountUtil;

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
        String url = "/index.jsp";

        if (action == null) {
            resp.sendRedirect(url);
        } else if (action.equals("showDescription")) {
            url = showDescription(req, resp);
            getServletContext().getRequestDispatcher(url).forward(req, resp);
        } else if (action.equals("openBook")) {
            url = openBook(req, resp);
            if (url != null && !url.equals("")) {
                getServletContext().getRequestDispatcher(url).forward(req, resp);
            }
        } else if (action.equals("addToDownloadList")) {
        }

    }

    private String showDescription(HttpServletRequest req, HttpServletResponse resp) {
        String url;
        String isbn13 = req.getParameter("isbn13");

        if (isbn13 == null || isbn13.equals("")) {
            url = "/index.jsp";
        } else {
            try {
                Book book = BookDB.selectBook(isbn13);
                book.setDescription(req.getServletContext().getRealPath(book.getDescription()));
                if (book.getDescription() != null && !book.getDescription().equals("")) {
                    book.readDescription();
                }
                req.setAttribute("book", book);
                url = "/catalog/description.jsp";
            } catch (SQLException e) {
                e.printStackTrace();
                url = "/index.jsp";
            } catch (IOException e) {
                e.printStackTrace();
                url = "/index.jsp";
            }
        }

        return url;
    }

    private String openBook(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String url = "";
        String isbn13 = req.getParameter("isbn13");

        if (isbn13 != null && !isbn13.equals("")) {
            try {
                Book book = BookDB.selectBook(isbn13);
                book.setPopularity(book.getPopularity() + 1);
                BookDB.updateBook(book);
                for (BookFile file : book.getFiles()) {
                    if (file.getFormat() == Format.PDF) {
                        String path = getServletContext().getRealPath(file.getPath());
                        File pdfFile = new File(path).getAbsoluteFile();
                        Files.copy(pdfFile.toPath(), resp.getOutputStream());
                        url = "";
                        break;
                    } else {
                        url = "/catalog/description.jsp";
                    }
                }
//                resp.setHeader("Content-Type", getServletContext().getMimeType(file.getName()));
//                resp.setHeader("Content-Length", String.valueOf(file.length()));
//                resp.setHeader("Content-Disposition", "inline; filename=\"" + file.getName() + "\"");
            } catch (SQLException e) {
                e.printStackTrace();
                url = "/catalog/description.jsp";
            }
        } else {
            url = "/catalog/description.jsp";
        }

        return url;
    }

    private String addToDownloadList(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        Account account = (Account) session.getAttribute("account");

        String url;
        String isbn13 = req.getParameter("isbn13");


        if (AccountUtil.sessionAccountExists(account, req, resp)) {

        } else {

        }

    }

}
