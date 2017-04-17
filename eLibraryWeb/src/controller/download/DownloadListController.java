package controller.download;

import entities.Book;
import entities.user.User;
import service.LibraryService;
import util.IOUtil;
import util.MailUtil;
import util.SessionUtil;
import util.validation.DataValidationException;
import util.validation.InternalDataValidationException;

import javax.ejb.EJB;
import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name = "downloadList", urlPatterns = "/download/downloadList")
public class DownloadListController extends HttpServlet {
    @EJB
    LibraryService service;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url;
        String action = req.getParameter("action");

        try {
            if (action == null) {
                resp.sendError(404);
                return;
            } else if (action.equals("showDownloadList")) {
                url = showDownloadList(req, resp);
            } else if (action.equals("addToDownloadList")) {
                url = addToDownloadList(req, resp);
            } else if (action.equals("deleteFromDownloadList")) {
                url = deleteFromDownloadList(req, resp);
            } else if (action.equals("download")) {
                url = downloadBooks(req, resp);
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

        if (url.trim().equals("")) {
            resp.sendRedirect("/download/downloadList.jsp");
        } else {
            req.getServletContext().getRequestDispatcher(url).forward(req, resp);
        }
    }

    private String showDownloadList(HttpServletRequest req, HttpServletResponse resp) {
        String url;

        url = "/download/downloadList.jsp";

        return url;
    }

    private String addToDownloadList(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, IOException,
            DataValidationException, InternalDataValidationException {
        String url;
        String isbn13 = req.getParameter("isbn13");

        User user = SessionUtil.getSessionAccount(req);
        List<Book> downloadList = user.getBooks();
        Book book = service.findBookByIsbn(isbn13);

        if (book != null) {
            if (user.getBooks().stream()
                    .filter(b -> b.getIsbn13().equals(book.getIsbn13())).collect(Collectors.toList()).size() == 0) {
                downloadList.add(book);
                if (!book.getDescription().trim().equals("")) {
                    req.setAttribute("bookDescription",
                            IOUtil.readDescriptionFile(
                                    req.getServletContext().getRealPath(book.getDescription())));
                }
                service.saveUser(user);
            }
            req.setAttribute("book", book);
            url = "/catalog/description?action=showDescription&amp;isbn13=" + isbn13;
        } else {
            throw new InternalDataValidationException("Incorrect book isbn13");
        }

        return url;
    }

    private String deleteFromDownloadList(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, DataValidationException {
        User user = SessionUtil.getSessionAccount(req);
        int bookIndex = getBookIndex(user, req.getParameter("isbn13"));
        if (bookIndex != -1) {
            user.getBooks().remove(bookIndex);
        }
        service.saveUser(user);

        return "";
    }

    private String downloadBooks(HttpServletRequest req, HttpServletResponse resp) {
        // TODO: 2016-05-18 make proper exception handling
        new Thread(() -> {
            User user = SessionUtil.getSessionAccount(req);
            String zipFile;
            try {
                zipFile = IOUtil.createDownloadsZip(user, req.getServletContext(), service);
                user.getBooks().clear();
                MailUtil.sendDownloadZip(user.getEmail(),
                        user.getUsername(),
                        "elibraryprojectfamcs@gmail.com",
                        "Book download", zipFile);
            } catch (IOException | InternalDataValidationException
                    | SQLException | MessagingException e) {
                e.printStackTrace();
            }
        }).start();

        return "/download/downloadConfirm.jsp";
    }

    private int getBookIndex(User user, String isbn13) {
        int flag = -1;
        List<Book> books = user.getBooks();
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getIsbn13().equals(isbn13)) {
                flag = i;
                break;
            }
        }
        return flag;
    }
}
