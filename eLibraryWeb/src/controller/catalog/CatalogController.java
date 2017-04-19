package controller.catalog;


import entities.Book;
import entities.Category;
import service.LibraryService;
import util.validation.DataValidationException;
import util.validation.InternalDataValidationException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@WebServlet(name = "catalog", urlPatterns = "/catalog")
public class CatalogController extends HttpServlet {
    @EJB
    LibraryService service;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String search = req.getParameter("search");
        String categoryName = req.getParameter("category");
        String url;

        try {
            if (search != null) {
                url = searchForTitle(search, req, resp);
            } else if (categoryName != null && !categoryName.equals("")) {
                url = showCategory(categoryName, req, resp);
            } else {
                url = showCatalog(req, resp);
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

        getServletContext().getRequestDispatcher(url).forward(req, resp);
    }

    private String showCatalog(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, DataValidationException, InternalDataValidationException {
        boolean mostPopular = Boolean.valueOf(req.getParameter("mostPopular"));
        boolean mostRecent = Boolean.valueOf(req.getParameter("mostRecent"));

        Stream<Book> stream = service.findAllBooks().stream();
        if (mostPopular) stream = stream.sorted(((o1, o2) -> Long.compare(o2.getPopularity(), o1.getPopularity())));
        if (mostRecent) stream = stream.sorted(((o1, o2) -> Integer.compare(o2.getPubYear(), o1.getPubYear())));
        req.setAttribute("books", stream.collect(Collectors.toList()));

        return "/catalog/catalog.jsp";
    }

    private String searchForTitle(String searchString, HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, DataValidationException, InternalDataValidationException {
        String filterTitle = searchString.toLowerCase();
        List<Book> books = service.findAllBooks().stream()
                .filter(book -> book.getTitle().toLowerCase().contains(filterTitle))
                .collect(Collectors.toList());
        req.setAttribute("books", books);

        return "/catalog/catalog.jsp";
    }

    private String showCategory(String categoryName, HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, DataValidationException, InternalDataValidationException {
        Category category = new Category(categoryName, null);
        List<Book> books = service.findAllBooks().stream()
                .filter(book -> book.getCategories().contains(category)).collect(Collectors.toList());
        req.setAttribute("books", books);

        return "/catalog/catalog.jsp";
    }
}
