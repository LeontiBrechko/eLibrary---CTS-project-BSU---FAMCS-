package service;

import entities.Author;
import entities.Book;
import entities.Category;
import entities.Publisher;
import entities.user.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class JpaLibraryService implements LibraryService {
    @PersistenceContext(name = "eLibrary")
    private EntityManager em;

    @Override
    public void saveUser(User user) {
        if (user.getId() == null) {
            em.persist(user);
        } else {
            em.merge(user);
        }
    }

    @Override
    public List<User> findAllUsers() {
        return em.createNamedQuery("User.findAll", User.class).getResultList();
    }

    @Override
    public User findUserByUsername(String username) {
        Query query = em.createNamedQuery("User.findByUsername", User.class);
        query.setParameter("username", username);
        List users = query.getResultList();
        if (users.size() != 1) return null;
        else return (User) users.get(0);
    }

    @Override
    public User findUserByEmail(String email) {
        Query query = em.createNamedQuery("User.findByEmail", User.class);
        query.setParameter("email", email);
        List users = query.getResultList();
        if (users.size() != 1) return null;
        else return (User) users.get(0);
    }

    @Override
    public void saveBook(Book book) {
        if (book.getId() == null) {
            em.persist(book);
        } else {
            em.merge(book);
        }
    }

    @Override
    public void removeBook(Book book) {
        em.remove(em.merge(book));
    }

    @Override
    public List<Book> findAllBooks() {
        return em.createNamedQuery("Book.findAll", Book.class).getResultList();
    }

    @Override
    public Book findBookByIsbn(String isbn13) {
        Query query = em.createNamedQuery("Book.findByIsbn", Book.class);
        query.setParameter("isbn", isbn13);
        List books = query.getResultList();
        if (books.size() != 1) return null;
        else return (Book) books.get(0);
    }

    @Override
    public void saveAuthor(Author author) {
        if (author.getId() == null) {
            em.persist(author);
        } else {
            em.merge(author);
        }
    }

    @Override
    public Author findAuthorByName(String firstName, String lastName) {
        Query query = em.createNamedQuery("Author.findByName", Author.class);
        query.setParameter("firstName", firstName);
        query.setParameter("lastName", lastName);
        List authors = query.getResultList();
        if (authors.size() != 1) return null;
        else return (Author) authors.get(0);
    }

    @Override
    public void savePublisher(Publisher publisher) {
        if (publisher.getId() == null) {
            em.persist(publisher);
        } else {
            em.merge(publisher);
        }
    }

    @Override
    public Publisher findPublisherByName(String name) {
        Query query = em.createNamedQuery("Publisher.findByName", Publisher.class);
        query.setParameter("name", name);
        List publishers = query.getResultList();
        if (publishers.size() != 1) return null;
        else return (Publisher) publishers.get(0);
    }

    @Override
    public List<Category> findAllCategories() {
        return em.createNamedQuery("Category.findAll", Category.class).getResultList();
    }
}
