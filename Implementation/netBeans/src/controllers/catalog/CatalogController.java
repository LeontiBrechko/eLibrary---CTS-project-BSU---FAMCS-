package controllers.catalog;

import data.BookDB;
import models.Book;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by Leonti on 2016-03-07.
 */
@WebServlet(name = "catalog", urlPatterns = "/catalog")
public class CatalogController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String search = req.getParameter("search");
        String category = req.getParameter("category");
        String url;

        try {
            if (search != null && !search.equals("")) {
                url = searchForTitle(search, req, resp);
            } else if (category != null && !category.equals("")) {
                url = showCategory(category, req, resp);
            } else {
                url = showCatalog(req, resp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // TODO: 2016-03-11 error message
            url = "index.jsp";
        }

        getServletContext().getRequestDispatcher(url).forward(req, resp);
    }

    private String showCatalog(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException {
        boolean mostPopular = Boolean.valueOf(req.getParameter("mostPopular"));
        boolean mostRecent = Boolean.valueOf(req.getParameter("mostRecent"));

        List<Book> books = BookDB.selectBookList(mostRecent, mostPopular);
        req.setAttribute("books", books);

        return "/catalog/catalog.jsp";
    }

    private String searchForTitle(String searchString, HttpServletRequest req, HttpServletResponse resp)
            throws SQLException {
        List<Book> books = BookDB.searchForTitle(searchString);
        req.setAttribute("books", books);

        return "/catalog/catalog.jsp";
    }

    private String showCategory(String categoryName, HttpServletRequest req, HttpServletResponse resp)
            throws SQLException {
        List<Book> books = BookDB.selectBookCategoryList(categoryName);
        req.setAttribute("books", books);

        return "/catalog/catalog.jsp";
    }
}
