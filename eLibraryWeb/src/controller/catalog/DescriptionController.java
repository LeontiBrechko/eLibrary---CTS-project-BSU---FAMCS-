package controller.catalog;

import entities.Book;
import entities.BookFile;
import entities.Category;
import entities.user.User;
import service.LibraryService;
import util.IOUtil;
import util.SessionUtil;
import util.validation.DataValidationException;
import util.validation.InternalDataValidationException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name = "bookDescription", urlPatterns = "/catalog/description")
public class DescriptionController extends HttpServlet {
    @EJB
    LibraryService service;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        String url;

        try {
            String isbn13 = req.getParameter("isbn13");
            Book book;
            if (isbn13 != null) {
                book = service.findBookByIsbn(isbn13);
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

            List<Category> categories = service.findAllCategories();
            req.setAttribute("categories", categories);
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
//        req.setAttribute("bookDescription",
//                IOUtil.readDescriptionFile(req.getServletContext().getRealPath(book.getDescription())));

        User user = SessionUtil.getSessionAccount(req);
        boolean isAddable = true;
        if (user != null && user.getBooks().stream()
                .filter(b -> b.getIsbn13().equals(book.getIsbn13())).collect(Collectors.toList()).size() != 0) {
            isAddable = false;
        }

        boolean isOpenable = false;
        for (BookFile file : book.getBookFiles()) {
            if (file.getFormat().getName().equals("PDF")) {
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

        for (BookFile file : book.getBookFiles()) {
            if (file.getFormat().getName().equals("PDF")) {
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
