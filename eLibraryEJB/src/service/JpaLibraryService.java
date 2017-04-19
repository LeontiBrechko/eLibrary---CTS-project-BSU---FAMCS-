package service;

import entities.*;
import entities.user.User;
import entities.user.UserRole;

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
            for (UserRole role : user.getRoles()) {
                role.setUserId(user.getId());
                em.persist(role);
            }
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
    public List<Author> findAllAuthors() {
        return em.createNamedQuery("Author.findAll", Author.class).getResultList();
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
    public List<Publisher> findAllPublishers() {
        return em.createNamedQuery("Publisher.findAll", Publisher.class).getResultList();
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

    @Override
    public Category findCategoryByName(String name) {
        Query query = em.createNamedQuery("Category.findByName", Category.class);
        query.setParameter("name", name);
        List categories = query.getResultList();
        if (categories.size() != 1) return null;
        else return (Category) categories.get(0);
    }

    @Override
    public List<Format> findAllFormats() {
        return em.createNamedQuery("Format.findAll", Format.class).getResultList();
    }

    @Override
    public Format findFormatByName(String name) {
        Query query = em.createNamedQuery("Format.findByName", Format.class);
        query.setParameter("name", name);
        List formats = query.getResultList();
        if (formats.size() != 1) return null;
        else return (Format) formats.get(0);
    }

    @Override
    public List<Language> findAllLanguages() {
        return em.createNamedQuery("Language.findAll", Language.class).getResultList();
    }

    @Override
    public Language findLanguageByName(String name) {
        Query query = em.createNamedQuery("Language.findByName", Language.class);
        query.setParameter("name", name);
        List languages = query.getResultList();
        if (languages.size() != 1) return null;
        else return (Language) languages.get(0);
    }
}
