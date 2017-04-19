package controller.admin;


import entities.*;
import service.LibraryService;
import util.IOUtil;
import util.SessionUtil;
import util.validation.DataValidationException;
import util.validation.InternalDataValidationException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

@WebServlet(name = "bookManagement", urlPatterns = "/admin/bookManagement")
@MultipartConfig
public class BookManagementController extends HttpServlet {
    @EJB
    LibraryService service;

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
                    req.setAttribute("publishers", service.findAllPublishers());
                    url = "/admin/books/bookUpdate/bookPublisher.jsp";
                } else if (nextStep.equals("review")) {
                    Book book = SessionUtil.getBookToUpdate(req);
                    if (book.getBookFiles() == null || book.getBookFiles().size() == 0) {
                        req.setAttribute("formats", service.findAllFormats());
                        req.setAttribute("languages", service.findAllLanguages());
                        url = "/admin/books/bookUpdate/bookFiles.jsp";
                        throw new DataValidationException("Please, select at least one Publisher or add new one.");
                    }
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
        List<Book> books = service.findAllBooks();
        req.setAttribute("books", books);
        return "/admin/books/libraryBooks.jsp";
    }

    private String getUpdateBook(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, DataValidationException, InternalDataValidationException {
        String isbn13 = req.getParameter("isbn13");

        Book book = service.findBookByIsbn(isbn13);
        SessionUtil.setBookToUpdate(book, req);
        List<Category> categories = service.findAllCategories();
        req.setAttribute("categories", categories);
        return "/admin/books/bookUpdate/bookMainInfo.jsp";
    }

    private String deleteBook(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, DataValidationException, InternalDataValidationException {
        String isbn13 = req.getParameter("isbn13");

        Book book = new Book();
        book.setIsbn13(isbn13);

        service.removeBook(book);

        return showLibraryBooks(req, resp);
    }

    private String doUpdateBook(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException,
            DataValidationException, InternalDataValidationException {
        Book book = SessionUtil.getBookToUpdate(req);

        String isbn13 = req.getParameter("isbn13");
        String title = req.getParameter("title");
        int yearPublished;
        try {
            yearPublished = Integer.parseInt(req.getParameter("yearPublished"));
        } catch (NumberFormatException e) {
            throw new DataValidationException("Please, enter valid year published " +
                    "(numeric and grater than 0)");
        }

        if (book == null) {
            book = new Book(isbn13, title, yearPublished,
                    null, new HashSet<>(),
                    null, null, new HashSet<>(),
                    new HashSet<>(), new HashSet<>());
        } else {
            book.setIsbn13(isbn13);
            book.setTitle(title);
            book.setPubYear(yearPublished);
        }

        String[] categoriesStrings = req.getParameterValues("selectedCategories");
        Set<Category> categories = new HashSet<>();
        if (categoriesStrings != null) {
            for (String categoryString : categoriesStrings) {
                Category category = service.findCategoryByName(categoryString);
                categories.add(category);
            }
        }

        book.setCategories(categories);

        Part filePart = req.getPart("description");
        String path;
        if (!Objects.equals(filePart.getSubmittedFileName().trim(), "")) {
            path = req.getServletContext().getRealPath("/catalog/books/" + isbn13 + "/desc.txt");
            IOUtil.writeFileProperty(filePart, path);
            book.setDescription("/catalog/books/" + isbn13 + "/desc.txt");
        }
        filePart = req.getPart("image");
        if (!Objects.equals(filePart.getSubmittedFileName().trim(), "")) {
            path = req.getServletContext().getRealPath("/catalog/books/" + isbn13 + "/image.jpg");
            IOUtil.writeFileProperty(filePart, path);
            book.setImagePath("/catalog/books/" + isbn13 + "/image.jpg");
        }
        filePart = req.getPart("thumbnail");
        if (!Objects.equals(filePart.getSubmittedFileName().trim(), "")) {
            path = req.getServletContext().getRealPath("/catalog/books/" + isbn13 + "/thumb.jpg");
            IOUtil.writeFileProperty(filePart, path);
            book.setThumbnail("/catalog/books/" + isbn13 + "/thumb.jpg");
        }

        SessionUtil.setBookToUpdate(book, req);

        req.setAttribute("categories", service.findAllCategories());
        req.setAttribute("authors", service.findAllAuthors());
        return "/admin/books/bookUpdate/bookAuthors.jsp";
    }

    private String updateBookAuthors(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, DataValidationException, InternalDataValidationException {
        Book book = SessionUtil.getBookToUpdate(req);
        Set<Author> authors = book.getAuthors();

        String updateType = req.getParameter("updateType");
        if (updateType.equals("selectAuthor")) {
            String[] authorsNames = req.getParameterValues("selectedAuthors");
            if (authorsNames != null) {
                authors = new HashSet<>(authorsNames.length);
                for (int i = 0; i < authorsNames.length; i++) {
                    String[] nextAuthor = authorsNames[i].split(" ");
                    Author author = service.findAuthorByName(nextAuthor[0], nextAuthor[1]);
                    authors.add(author);
                }
                book.setAuthors(authors);
            }
        } else if (updateType.equals("addAuthor")) {
            Author author =
                    new Author(req.getParameter("firstName"),
                            req.getParameter("lastName"));
            if (service.findAuthorByName(author.getFirstName(), author.getLastName()) != null) {
                throw new DataValidationException("Author already exists in the system." +
                        "Please, use selection list to add the author");
            } else {
                service.saveAuthor(author);
            }

            authors.add(author);
        }

        req.setAttribute("authors", service.findAllAuthors());
        return "/admin/books/bookUpdate/bookAuthors.jsp";
    }

    private String updateBookPublisher(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, DataValidationException, InternalDataValidationException {
        Book book = SessionUtil.getBookToUpdate(req);

        req.setAttribute("publishers", service.findAllPublishers());
        String updateType = req.getParameter("updateType");
        if (updateType.equals("selectPublisher")) {
            Publisher publisher = service.findPublisherByName(req.getParameter("selectedPublisher"));
            if (publisher == null) {
                throw new DataValidationException("Please, select at least one Publisher or add new one.");
            }
            book.setPublishers(new HashSet<>(Arrays.asList(publisher)));
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

            if (service.findUserByUsername(publisher.getName()) != null) {
                throw new DataValidationException("Publisher already exists in the system." +
                        "Please, use selection list to add the publisher");
            } else {
                service.savePublisher(publisher);
            }

            book.setPublishers(new HashSet<>(Arrays.asList(publisher)));
        }

        req.setAttribute("formats", service.findAllFormats());
        req.setAttribute("languages", service.findAllLanguages());
        return "/admin/books/bookUpdate/bookFiles.jsp";
    }

    private String addBookFile(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException,
            DataValidationException, InternalDataValidationException {
        Book book = SessionUtil.getBookToUpdate(req);

        Format format = service.findFormatByName(req.getParameter("selectedFormat"));
        Language language = service.findLanguageByName(req.getParameter("selectedLanguage"));
        String path = "/catalog/books/" + book.getIsbn13() + "/" +
                book.getTitle().replace(" ", "_") + "." + format.getName().toLowerCase();
        BookFile bookFile = new BookFile(format, language, path);

        IOUtil.writeFileProperty(req.getPart("file"),
                req.getServletContext().getRealPath(path));

        book.getBookFiles().add(bookFile);

        return "/admin/books/bookUpdate/bookFiles.jsp";
    }

    private String deleteBookFile(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, DataValidationException, InternalDataValidationException {
        Book book = SessionUtil.getBookToUpdate(req);

        Format format = new Format(req.getParameter("format"));
        Language language = new Language(req.getParameter("language"));
        String path = req.getParameter("path");
        BookFile bookFileToDelete = new BookFile(format, language, path);

        if (book.getBookFiles().contains(bookFileToDelete)) {
            book.getBookFiles().remove(bookFileToDelete);
            new File(req.getServletContext().getRealPath(bookFileToDelete.getPath())).delete();
        }

        return "/admin/books/bookUpdate/bookFiles.jsp";
    }

    private String updateBook(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, DataValidationException, InternalDataValidationException {
        Book book = SessionUtil.getBookToUpdate(req);
        if (service.findBookByIsbn(book.getIsbn13()) != null) {
            service.removeBook(service.findBookByIsbn(book.getIsbn13()));
        }

        service.saveBook(book);
        SessionUtil.deleteBookToUpdate(req);

        return showLibraryBooks(req, resp);
    }
}
