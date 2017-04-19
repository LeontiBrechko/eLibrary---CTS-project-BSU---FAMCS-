package service;

import entities.*;
import entities.user.User;

import javax.ejb.Local;
import java.util.List;

@Local
public interface LibraryService {
    void saveUser(User user);

    List<User> findAllUsers();

    User findUserByUsername(String username);

    User findUserByEmail(String email);

    void saveBook(Book book);

    void removeBook(Book book);

    List<Book> findAllBooks();

    Book findBookByIsbn(String isbn13);

    void saveAuthor(Author author);

    List<Author> findAllAuthors();

    Author findAuthorByName(String firstName, String lastName);

    void savePublisher(Publisher publisher);

    List<Publisher> findAllPublishers();

    Publisher findPublisherByName(String name);

    List<Category> findAllCategories();

    Category findCategoryByName(String name);

    List<Format> findAllFormats();

    Format findFormatByName(String name);

    List<Language> findAllLanguages();

    Language findLanguageByName(String name);
}
