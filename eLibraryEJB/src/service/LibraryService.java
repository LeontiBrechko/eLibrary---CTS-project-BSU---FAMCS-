package service;

import entities.Author;
import entities.Book;
import entities.Category;
import entities.Publisher;
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

    Author findAuthorByName(String firstName, String lastName);

    void savePublisher(Publisher publisher);

    Publisher findPublisherByName(String name);

    List<Category> findAllCategories();
}
