package controllers.admin;

import com.sun.org.apache.xml.internal.utils.URI;
import data.AuthorDB;
import data.BookDB;
import data.PublisherDB;
import models.*;
import models.enums.Format;
import models.enums.Language;
import utils.dataValidation.DataValidationException;
import utils.dataValidation.InternalDataValidationException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
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
        String url;

        try {
            if (action == null) {
                resp.sendError(404);
                return;
            } else if (action.equals("showLibraryBooks")) {
                url = showLibraryBooks(req, resp);
            } else if (action.equals("updateBook")) {
                url = getUpdateBook(req, resp);
            } else if (action.equals("deleteBook")) {
                url = deleteBook(req, resp);
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

        req.getServletContext().getRequestDispatcher(url).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        String url = "";

        try {
            if (action == null) {
                resp.sendError(404);
                return;
            } else if (action.equals("updateBookMainInfo")) {
                url = "/admin/books/bookUpdate/bookMainInfo.jsp";
                url = doUpdateBook(req, resp);
            } else if (action.equals("updateBookAuthors")) {
                url = "/admin/books/bookUpdate/bookAuthors.jsp";
                url = updateBookAuthors(req, resp);
            } else if (action.equals("updateBookPublisher")) {
                url = "/admin/books/bookUpdate/bookPublisher.jsp";
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
                    resp.sendError(404);
                    return;
                } else if (nextStep.equals("publisher")) {
                    url = "/admin/books/bookUpdate/bookPublisher.jsp";
                } else if (nextStep.equals("review")) {
                    url = "/admin/books/bookUpdate/bookUpdateReview.jsp";
                } else {
                    resp.sendError(404);
                    return;
                }
            } else {
                resp.sendError(404);
                return;
            }
        } catch (DataValidationException e) {
            req.setAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            log(e.getMessage(), e);
            for (Throwable t : e.getSuppressed()) {
                log(t.getMessage(), t);
            }
            resp.sendError(500);
            return;
        }

        req.getServletContext().getRequestDispatcher(url).forward(req, resp);
    }

    private String showLibraryBooks(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, DataValidationException, InternalDataValidationException {
        List<Book> books = BookDB.selectBookList(false, false);
        req.setAttribute("books", books);
        return "/admin/books/libraryBooks.jsp";
    }

    private String getUpdateBook(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, DataValidationException, InternalDataValidationException {
        String isbn13 = req.getParameter("isbn13");

        Book book = BookDB.selectBook(isbn13);
        Book.setBookToUpdate(book, req);
        return "/admin/books/bookUpdate/bookMainInfo.jsp";
    }

    private String deleteBook(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, DataValidationException, InternalDataValidationException {
        String isbn13 = req.getParameter("isbn13");

        BookDB.deleteBook(isbn13);

        return showLibraryBooks(req, resp);
    }

    private String doUpdateBook(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException,
            DataValidationException, InternalDataValidationException {
        Book book = Book.getBookToUpdate(req);

        String isbn13 = req.getParameter("isbn13");
        String title = req.getParameter("title");
        String description = null;
        String image = null;
        String thumbnail = null;
        int yearPublished;
        try {
            yearPublished = Integer.parseInt(req.getParameter("yearPublished"));
        } catch (NumberFormatException e) {
            throw new DataValidationException("Please, enter valid year published " +
                    "(numeric and grater than 0)");
        }

        if (book == null) {
            book = new Book(isbn13, title, yearPublished,
                    description, new ArrayList<>(),
                    image, thumbnail, new Publisher("Temp", "Temp", "Temp", null, 0, null),
                    new ArrayList<>(), new ArrayList<>());
        } else {
            book.setIsbn13(isbn13);
            book.setTitle(title);
            book.setYearPublished(yearPublished);
            book.setDescription(description);
            book.setImage(image);
            book.setThumbnail(thumbnail);
        }

        String[] categoriesStrings = req.getParameterValues("selectedCategories");
        List<Category> categories = new ArrayList<>();
        if (categoriesStrings != null) {
            for (String categoryString : categoriesStrings) {
                Category category = new Category(categoryString, null);
                categories.add(category);
            }
        }

        book.setCategories(categories);

        Part filePart = req.getPart("description");
        String path;
        if (filePart.getSubmittedFileName().trim() != "") {
            path = req.getServletContext().getRealPath("/catalog/books/" + isbn13 + "/desc.txt");
            book.writeFileProperty(filePart, path);
            book.setDescription("/catalog/books/" + isbn13 + "/desc.txt");
        }
        filePart = req.getPart("image");
        if (filePart.getSubmittedFileName().trim() != "") {
            path = req.getServletContext().getRealPath("/catalog/books/" + isbn13 + "/image.jpg");
            book.writeFileProperty(filePart, path);
            book.setImage("/catalog/books/" + isbn13 + "/image.jpg");
        }
        filePart = req.getPart("thumbnail");
        if (filePart.getSubmittedFileName().trim() != "") {
            path = req.getServletContext().getRealPath("/catalog/books/" + isbn13 + "/thumb.jpg");
            book.writeFileProperty(filePart, path);
            book.setThumbnail("/catalog/books/" + isbn13 + "/thumb.jpg");
        }

        Book.setBookToUpdate(book, req);

        return "/admin/books/bookUpdate/bookAuthors.jsp";
    }

    private String updateBookAuthors(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, DataValidationException, InternalDataValidationException {
        Book book = Book.getBookToUpdate(req);
        List<Author> authors = book.getAuthors();

        String updateType = req.getParameter("updateType");
        if (updateType.equals("selectAuthor")) {
            String[] authorsNames = req.getParameterValues("selectedAuthors");
            authors = new ArrayList<>(authorsNames.length);
            for (int i = 0; i < authorsNames.length; i++) {
                String[] nextAuthor = authorsNames[i].split(" ");
                Author author = new Author(nextAuthor[0], nextAuthor[1]);
                authors.add(author);
            }
            book.setAuthors(authors);
        } else if (updateType.equals("addAuthor")) {
            Author author =
                    new Author(req.getParameter("firstName"),
                            req.getParameter("lastName"));
            if (AuthorDB.selectAuthorId(author) != - 1) {
                throw new DataValidationException("Author already exists in the system." +
                        "Please, use selection list to add the author");
            } else {
                AuthorDB.insertAuthor(author);
            }

            authors.add(author);
        }

        return "/admin/books/bookUpdate/bookAuthors.jsp";
    }

    private String updateBookPublisher(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, DataValidationException, InternalDataValidationException {
        Book book = Book.getBookToUpdate(req);

        String updateType = req.getParameter("updateType");
        if (updateType.equals("selectPublisher")) {
            Publisher publisher = PublisherDB.selectPublisher(req.getParameter("selectedPublisher"));
            book.setPublisher(publisher);
        } else if (updateType.equals("addPublisher")) {
            int streetNumber;
            try {
                streetNumber = Integer.parseInt(req.getParameter("streetNumber"));
            } catch (NumberFormatException e) {
                throw new DataValidationException("Please, enter valid street number " +
                        "(numeric and grater than 0)");
            }
            Publisher publisher =
                    new Publisher(req.getParameter("name"), req.getParameter("country"),
                            req.getParameter("city"), req.getParameter("state"),
                            streetNumber, req.getParameter("streetName"));

            if (PublisherDB.selectPublisherId(publisher) != - 1) {
                throw new DataValidationException("Publisher already exists in the system." +
                        "Please, use selection list to add the publisher");
            } else {
                PublisherDB.insertPublisher(publisher);
            }

            book.setPublisher(publisher);
        }

        return "/admin/books/bookUpdate/bookFiles.jsp";
    }

    private String addBookFile(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException,
            DataValidationException, InternalDataValidationException {
        Book book = Book.getBookToUpdate(req);

        Format format = Format.valueOf(req.getParameter("selectedFormat"));
        Language language = Language.valueOf(req.getParameter("selectedLanguage"));
        String path = "/catalog/books/" + book.getIsbn13() + "/" +
                book.getTitle().replace(" ", "_") + "." + format.name().toLowerCase();
        BookFile bookFile = new BookFile(format, language, path);

        book.writeFileProperty(req.getPart("file"),
                req.getServletContext().getRealPath(path));

        book.getFiles().add(bookFile);

        return "/admin/books/bookUpdate/bookFiles.jsp";
    }

    private String deleteBookFile(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, DataValidationException, InternalDataValidationException {
        Book book = Book.getBookToUpdate(req);

        Format format = Format.valueOf(req.getParameter("format"));
        Language language = Language.valueOf(req.getParameter("language"));
        String path = req.getParameter("path");
        BookFile bookFileToDelete = new BookFile(format, language, path);

        int index = book.getBookFileListIndex(bookFileToDelete);
        if (index >= 0) {
            book.getFiles().remove(index);
            new File(req.getServletContext().getRealPath(bookFileToDelete.getPath())).delete();
        }

        return "/admin/books/bookUpdate/bookFiles.jsp";
    }

    private String updateBook(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, DataValidationException, InternalDataValidationException{
        Book book = Book.getBookToUpdate(req);
        if (BookDB.selectBook(book.getIsbn13()) != null) {
            BookDB.deleteBook(book.getIsbn13());
        }
        BookDB.insertBook(book);
        Book.deleteBookToUpdate(req);

        return showLibraryBooks(req, resp);
    }
}
