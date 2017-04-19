package entities;

import javax.persistence.*;
import java.io.Serializable;


/**
 * The persistent class for the book_file database table.
 */
@Entity
@NamedQuery(name = "BookFile.findAll", query = "SELECT b FROM BookFile b")
public class BookFile extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "PATH", nullable = false)
    private String path;

    //bi-directional many-to-one association to Book
    @ManyToOne
    @JoinColumn(name = "BOOK_ID", referencedColumnName = "ID")
    private Book book;

    //bi-directional many-to-one association to Format
    @ManyToOne
    @JoinColumn(name = "FORMAT_ID", referencedColumnName = "ID")
    private Format format;

    //bi-directional many-to-one association to Language
    @ManyToOne
    @JoinColumn(name = "LANG_ID", referencedColumnName = "ID")
    private Language language;

    public BookFile() {
    }

    public BookFile(Format format, Language language, String path) {
        this.setFormat(format);
        this.setLanguage(language);
        this.setPath(path);
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Book getBook() {
        return this.book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Format getFormat() {
        return this.format;
    }

    public void setFormat(Format format) {
        this.format = format;
    }

    public Language getLanguage() {
        return this.language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BookFile bookFile = (BookFile) o;

        if (book != null ? !book.equals(bookFile.book) : bookFile.book != null) return false;
        if (format != null ? !format.equals(bookFile.format) : bookFile.format != null) return false;
        return language != null ? language.equals(bookFile.language) : bookFile.language == null;
    }

    @Override
    public int hashCode() {
        int result = book != null ? book.hashCode() : 0;
        result = 31 * result + (format != null ? format.hashCode() : 0);
        result = 31 * result + (language != null ? language.hashCode() : 0);
        return result;
    }
}