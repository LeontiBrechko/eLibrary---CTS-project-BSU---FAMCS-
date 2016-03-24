package controllers.admin;

import data.AuthorDB;
import data.BookDB;
import data.CategoryDB;
import data.PublisherDB;
import models.*;
import models.enums.Format;
import models.enums.Language;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
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
            } else if (action.equals("updateBookMainInfo")) {
                url = doUpdateBook(req, resp);
            } else if (action.equals("updateBookAuthors")) {
                url = updateBookAuthors(req, resp);
            } else if (action.equals("updateBookPublisher")) {
                url = updateBookPublisher(req, resp);
            } else if (action.equals("addBookFile")) {
                url = addBookFile(req, resp);
            } else if (action.equals("deleteBookFile")) {
                url = deleteBookFile(req, resp);
            } else if (action.equals("updateBook")) {
                url = updateBook(req, resp);
            } else if (action.equals("continue")) {
                String nextStep = req.getParameter("nextStep");
                if (nextStep == null || nextStep.equals("")) {
                    url = "/index.jsp";
                } else if (nextStep.equals("publisher")) {
                    url = "/admin/books/bookUpdate/bookPublisher.jsp";
                } else if (nextStep.equals("review")) {
                    url = "/admin/books/bookUpdate/bookUpdateReview.jsp";
                }
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
//        req.setAttribute("book", book);
        req.getSession().setAttribute("bookToUpdate", book);
        req.setAttribute("categories", CategoryDB.selectAllCategories());
        return "/admin/books/bookUpdate/bookMainInfo.jsp";
    }

    private String doUpdateBook(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, IOException, ServletException {
        String isbn13 = req.getParameter("isbn13");

        // TODO: 2016-03-17 check input data
        Book book = (Book) req.getSession().getAttribute("bookToUpdate");
        if (book == null) {
            book = new Book();
        }
        book.setIsbn13(isbn13);
        book.setTitle(req.getParameter("title"));
        book.setYearPublished(Integer.parseInt(req.getParameter("yearPublished")));
        book.setPopularity(Long.parseLong(req.getParameter("popularity")));
        book.setDescription("/catalog/books/" + isbn13 + "/desc.txt");
        book.setImage("/catalog/books/" + isbn13 + "/image.jpg");
        book.setThumbnail("/catalog/books/" + isbn13 + "/thumb.jpg");

        Part filePart = req.getPart("description");
        String path = req.getServletContext().getRealPath("/catalog/books/" + isbn13 + "/desc.txt");
        book.writeProperty(filePart, path);
        filePart = req.getPart("image");
        path = req.getServletContext().getRealPath("/catalog/books/" + isbn13 + "/image.jpg");
        book.writeProperty(filePart, path);
        filePart = req.getPart("thumbnail");
        path = req.getServletContext().getRealPath("/catalog/books/" + isbn13 + "/thumb.jpg");
        book.writeProperty(filePart, path);

        String[] categoriesStrings = req.getParameterValues("selectedCategories");
        List<Category> categories = new ArrayList<>();
        if (categoriesStrings != null) {
            for (String categoryString : categoriesStrings) {
                Category category = new Category();
                category.setName(categoryString);
                categories.add(category);
            }
        }

        book.setCategories(categories);

//        BookDB.updateBook(book);
//        req.setAttribute("book", book);
        req.getSession().setAttribute("bookToUpdate", book);

        return "/admin/books/bookUpdate/bookAuthors.jsp";
    }

    private String updateBookAuthors(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException {
        Book book = (Book) req.getSession().getAttribute("bookToUpdate");
        if (book == null) {
            // TODO: 2016-03-22 raise error
        }
        List<Author> authors = book.getAuthors();

        String updateType = req.getParameter("updateType");
        // TODO: 2016-03-21 send error if no appropriate use
        if (updateType.equals("selectAuthor")) {
            String[] authorsNames = req.getParameterValues("selectedAuthors");
            authors = new ArrayList<>(authorsNames.length);
            for (int i = 0; i < authorsNames.length; i++) {
                String[] nextAuthor = authorsNames[i].split(" ");
                Author author = new Author();
                author.setFirstName(nextAuthor[0]);
                author.setLastName(nextAuthor[1]);
                authors.add(author);
            }
            book.setAuthors(authors);
        } else if (updateType.equals("addAuthor")) {
            Author author = new Author();
            // TODO: 2016-03-22 check input
            author.setFirstName(req.getParameter("firstName"));
            author.setLastName(req.getParameter("lastName"));

            if (AuthorDB.selectAuthorId(author) != - 1) {
                // TODO: 2016-03-21 error
            } else {
                AuthorDB.insertAuthor(author);
            }

            authors.add(author);
        }

        return "/admin/books/bookUpdate/bookAuthors.jsp";
    }

    private String updateBookPublisher(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException {
        Book book = (Book) req.getSession().getAttribute("bookToUpdate");
        if (book == null) {
            // TODO: 2016-03-22 raise error
        }

        String updateType = req.getParameter("updateType");
        // TODO: 2016-03-21 send error if no appropriate use
        if (updateType.equals("selectPublisher")) {
            Publisher publisher = PublisherDB.selectPublisher(req.getParameter("selectedPublisher"));
            book.setPublisher(publisher);
        } else if (updateType.equals("addPublisher")) {
            // TODO: 2016-03-22 check input
            Publisher publisher = new Publisher();
            publisher.setName(req.getParameter("name"));
            publisher.setCountry(req.getParameter("country"));
            publisher.setState(req.getParameter("state"));
            publisher.setCity(req.getParameter("city"));
            if (req.getParameter("streetNumber") != null && !req.getParameter("streetNumber").equals("")) {
                publisher.setStreetNumber(Integer.parseInt(req.getParameter("streetNumber")));
            }
            publisher.setStreetName(req.getParameter("streetName"));

            if (PublisherDB.selectPublisherId(publisher) != - 1) {
                // TODO: 2016-03-21 error
            } else {
                PublisherDB.insertPublisher(publisher);
            }

            book.setPublisher(publisher);
        }

        return "/admin/books/bookUpdate/bookFiles.jsp";
    }

    private String addBookFile(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, IOException, ServletException {
        Book book = (Book) req.getSession().getAttribute("bookToUpdate");
        if (book == null) {
            // TODO: 2016-03-22 raise error
        }

        BookFile bookFile = new BookFile();
        bookFile.setFormat(Format.valueOf(req.getParameter("selectedFormat")));
        bookFile.setLanguage(Language.valueOf(req.getParameter("selectedLanguage")));
        bookFile.setPath("/catalog/books/" + book.getIsbn13() +
                "/" + book.getTitle().replace(" ", "_") + "." + bookFile.getFormat().name().toLowerCase());

        Part filePart = req.getPart("file");
        String path = req.getServletContext().getRealPath("/catalog/books/" + book.getIsbn13() +
                "/" + book.getTitle().replace(" ", "_") + "." + bookFile.getFormat().name().toLowerCase());
        book.writeProperty(filePart, path);

        book.getFiles().add(bookFile);

        return "/admin/books/bookUpdate/bookFiles.jsp";
    }

    private String deleteBookFile(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException {
        Book book = (Book) req.getSession().getAttribute("bookToUpdate");
        if (book == null) {
            // TODO: 2016-03-22 raise error
        }

        BookFile bookFileToDelete = new BookFile();
        bookFileToDelete.setFormat(Format.valueOf(req.getParameter("format")));
        bookFileToDelete.setLanguage(Language.valueOf(req.getParameter("language")));
        bookFileToDelete.setPath(req.getParameter("path"));
        int index = book.getBookFileIndex(bookFileToDelete);
        if (index >= 0) {
            book.getFiles().remove(index);
            new File(req.getServletContext().getRealPath(bookFileToDelete.getPath())).delete();
        }

        return "/admin/books/bookUpdate/bookFiles.jsp";
    }

    private String updateBook(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException {
        Book book = (Book) req.getSession().getAttribute("bookToUpdate");
        if (book == null) {
            // TODO: 2016-03-22 raise error
        }

        book.setIsbn13("test");
        BookDB.insertBook(book);

        return showLibraryBooks(req, resp);
    }

//    private String deleteBook(HttpServletRequest req, HttpServletResponse resp)
//            throws SQLException {
//        String isbn13 = req.getParameter("isbn13");
//
//    }
}
