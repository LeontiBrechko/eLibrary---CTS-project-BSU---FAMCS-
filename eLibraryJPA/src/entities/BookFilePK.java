package entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * The primary key class for the book_file database table.
 */
@Embeddable
public class BookFilePK implements Serializable {
    //default serial version id, required for serializable classes.
    private static final long serialVersionUID = 1L;

    @Column(name = "BOOK_ID", insertable = false, updatable = false)
    private String bookId;

    @Column(name = "FORMAT_ID", insertable = false, updatable = false)
    private String formatId;

    @Column(name = "LANG_ID", insertable = false, updatable = false)
    private String langId;

    public BookFilePK() {
    }

    public String getBookId() {
        return this.bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getFormatId() {
        return this.formatId;
    }

    public void setFormatId(String formatId) {
        this.formatId = formatId;
    }

    public String getLangId() {
        return this.langId;
    }

    public void setLangId(String langId) {
        this.langId = langId;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof BookFilePK)) {
            return false;
        }
        BookFilePK castOther = (BookFilePK) other;
        return
                this.bookId.equals(castOther.bookId)
                        && this.formatId.equals(castOther.formatId)
                        && this.langId.equals(castOther.langId);
    }

    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + this.bookId.hashCode();
        hash = hash * prime + this.formatId.hashCode();
        hash = hash * prime + this.langId.hashCode();

        return hash;
    }
}