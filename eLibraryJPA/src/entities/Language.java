package entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;


/**
 * The persistent class for the language database table.
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "Language.findAll", query = "SELECT l FROM Language l"),
        @NamedQuery(name = "Language.findByName", query = "SELECT l FROM Language l WHERE l.name = :name")
})
public class Language extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;

    //bi-directional many-to-one association to BookFile
    @OneToMany
    @JoinColumn(name = "LANG_ID", referencedColumnName = "ID")
    private List<BookFile> bookFiles;

    public Language() {
    }

    public Language(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<BookFile> getBookFiles() {
        return this.bookFiles;
    }

    public void setBookFiles(List<BookFile> bookFiles) {
        this.bookFiles = bookFiles;
    }

    public BookFile addBookFile(BookFile bookFile) {
        getBookFiles().add(bookFile);
        bookFile.setLanguage(this);

        return bookFile;
    }

    public BookFile removeBookFile(BookFile bookFile) {
        getBookFiles().remove(bookFile);
        bookFile.setLanguage(null);

        return bookFile;
    }

}