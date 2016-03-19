package controllers.admin;

import data.BookDB;
import models.Book;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Leonti on 2016-03-15.
 */

@WebServlet(name = "bookManagement", urlPatterns = "/admin/bookManagement")
@MultipartConfig
public class BookManagementController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        String url = "";

        try {
            if (action == null || action.equals("")) {
                url = "/index.jsp";
            } else if (action.equals("showLibraryBooks")) {
                url = showLibraryBooks(req, resp);
            } else if (action.equals("updateBook")) {
                url = getUpdateBook(req, resp);
            }
        } catch (SQLException e) {
            // TODO: 2016-03-15 error message
            e.printStackTrace();
        }

        if (!url.equals("/index.jsp")) {
            req.getServletContext().getRequestDispatcher(url).forward(req, resp);
        } else {
            resp.sendRedirect(url);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        String url = "";

        try {
            if (action == null || action.equals("")) {
                url = "/index.jsp";
            } else if (action.equals("updateBook")) {
                url = doUpdateBook(req, resp);
            }
        } catch (SQLException e) {
            // TODO: 2016-03-15 error message
            e.printStackTrace();
        }

        if (!url.equals("/index.jsp")) {
            req.getServletContext().getRequestDispatcher(url).forward(req, resp);
        } else {
            resp.sendRedirect(url);
        }
    }

    private String showLibraryBooks(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException {
        List<Book> books = BookDB.selectBookList(false, false);
        req.setAttribute("books", books);
        return "/admin/books/libraryBooks.jsp";
    }

    private String getUpdateBook(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException {
        String isbn13 = req.getParameter("isbn13");

        Book book = BookDB.selectBook(isbn13);
        req.setAttribute("book", book);
        return "/admin/books/bookUpdate.jsp";
    }

    private String doUpdateBook(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, IOException, ServletException {
        String isbn13 = req.getParameter("isbn13");

        // TODO: 2016-03-17 check input data
        Book book = new Book();
        book.setIsbn13(isbn13);
        book.setTitle(req.getParameter("title"));
        book.setYearPublished(Integer.parseInt(req.getParameter("yearPublished")));
        book.setPopularity(Long.parseLong(req.getParameter("popularity")));
        book.setDescription("/catalog/books/" + isbn13 + "desc.txt");
        book.setImage("/catalog/books/" + isbn13 + "image.jpg");
        book.setThumbnail(req.getParameter("/catalog/books/" + isbn13 + "thumb.jpg"));

        Part filePart = req.getPart("description");
        String path = req.getServletContext().getRealPath("/catalog/books/" + isbn13 + "/desc.txt");
        book.writeProperty(filePart, path);

        filePart = req.getPart("image");
        path = req.getServletContext().getRealPath("/catalog/books/" + isbn13 + "/image.jpg");
        book.writeProperty(filePart, path);

        filePart = req.getPart("thumbnail");
        path = req.getServletContext().getRealPath("/catalog/books/" + isbn13 + "/thumb.jpg");
        book.writeProperty(filePart, path);

        BookDB.updateBook(book);
        req.setAttribute("book", book);

        return "/admin/books/bookUpdate.jsp";
    }

//    private String deleteBook(HttpServletRequest req, HttpServletResponse resp)
//            throws SQLException {
//        String isbn13 = req.getParameter("isbn13");
//
//    }
}
