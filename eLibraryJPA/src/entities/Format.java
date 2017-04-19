package entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;


/**
 * The persistent class for the format database table.
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "Format.findAll", query = "SELECT f FROM Format f"),
        @NamedQuery(name = "Format.findByName", query = "SELECT f FROM Format f WHERE f.name = :name")
})
public class Format extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "NAME", nullable = false, unique = true)
    private String name;

    //bi-directional many-to-one association to BookFile
    @OneToMany
    @JoinColumn(name = "FORMAT_ID", referencedColumnName = "ID")
    private List<BookFile> bookFiles;

    public Format() {
    }

    public Format(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<BookFile> getBookFiles() {
        return bookFiles;
    }

    public void setBookFiles(List<BookFile> bookFiles) {
        this.bookFiles = bookFiles;
    }

    public BookFile addBookFile(BookFile bookFile) {
        getBookFiles().add(bookFile);
        bookFile.setFormat(this);

        return bookFile;
    }

    public BookFile removeBookFile(BookFile bookFile) {
        getBookFiles().remove(bookFile);
        bookFile.setFormat(null);

        return bookFile;
    }

}