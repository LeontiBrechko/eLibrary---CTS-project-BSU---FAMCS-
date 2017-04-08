package entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the language database table.
 * 
 */
@Entity
@NamedQuery(name="Language.findAll", query="SELECT l FROM Language l")
public class Language implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="LANG_ID")
	private String langId;

	private String name;

	//bi-directional many-to-one association to BookFile
	@OneToMany(mappedBy="language")
	private List<BookFile> bookFiles;

	public Language() {
	}

	public String getLangId() {
		return this.langId;
	}

	public void setLangId(String langId) {
		this.langId = langId;
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