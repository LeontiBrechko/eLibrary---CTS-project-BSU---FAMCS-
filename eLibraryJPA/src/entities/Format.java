package entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the format database table.
 * 
 */
@Entity
@NamedQuery(name="Format.findAll", query="SELECT f FROM Format f")
public class Format implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="FORMAT_ID")
	private String formatId;

	private String name;

	//bi-directional many-to-one association to BookFile
	@OneToMany(mappedBy="format")
	private List<BookFile> bookFiles;

	public Format() {
	}

	public String getFormatId() {
		return this.formatId;
	}

	public void setFormatId(String formatId) {
		this.formatId = formatId;
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
		bookFile.setFormat(this);

		return bookFile;
	}

	public BookFile removeBookFile(BookFile bookFile) {
		getBookFiles().remove(bookFile);
		bookFile.setFormat(null);

		return bookFile;
	}

}