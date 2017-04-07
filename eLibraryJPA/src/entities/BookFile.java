package entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the book_file database table.
 * 
 */
@Entity
@Table(name="book_file")
@NamedQuery(name="BookFile.findAll", query="SELECT b FROM BookFile b")
public class BookFile implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private BookFilePK id;

	private String path;

	//bi-directional many-to-one association to Book
	@ManyToOne
	@JoinColumn(name="BOOK_ID")
	private Book book;

	//bi-directional many-to-one association to Format
	@ManyToOne
	@JoinColumn(name="FORMAT_ID")
	private Format format;

	//bi-directional many-to-one association to Language
	@ManyToOne
	@JoinColumn(name="LANG_ID")
	private Language language;

	public BookFile() {
	}

	public BookFilePK getId() {
		return this.id;
	}

	public void setId(BookFilePK id) {
		this.id = id;
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

}