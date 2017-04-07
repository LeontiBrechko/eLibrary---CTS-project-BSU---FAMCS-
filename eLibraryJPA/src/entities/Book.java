package entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigInteger;
import java.util.List;


/**
 * The persistent class for the book database table.
 * 
 */
@Entity
@NamedQuery(name="Book.findAll", query="SELECT b FROM Book b")
public class Book implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="BOOK_ID")
	private String bookId;

	private String description;

	private String image;

	@Column(name="ISBN_13")
	private String isbn13;

	private BigInteger popularity;

	@Column(name="PUB_YEAR")
	private int pubYear;

	private String thumb;

	private String title;

	//bi-directional many-to-many association to Account
	@ManyToMany
	@JoinTable(
		name="account_download_list"
		, joinColumns={
			@JoinColumn(name="BOOK_ID")
			}
		, inverseJoinColumns={
			@JoinColumn(name="ACCOUNT_ID")
			}
		)
	private List<Account> accounts;

	//bi-directional many-to-many association to Author
	@ManyToMany
	@JoinTable(
		name="book_author"
		, joinColumns={
			@JoinColumn(name="BOOK_ID")
			}
		, inverseJoinColumns={
			@JoinColumn(name="AUTH_ID")
			}
		)
	private List<Author> authors;

	//bi-directional many-to-many association to Category
	@ManyToMany
	@JoinTable(
		name="book_category"
		, joinColumns={
			@JoinColumn(name="BOOK_ID")
			}
		, inverseJoinColumns={
			@JoinColumn(name="CATEGORY_ID")
			}
		)
	private List<Category> categories;

	//bi-directional many-to-many association to Publisher
	@ManyToMany
	@JoinTable(
		name="book_publisher"
		, joinColumns={
			@JoinColumn(name="BOOK_ID")
			}
		, inverseJoinColumns={
			@JoinColumn(name="PUB_ID")
			}
		)
	private List<Publisher> publishers;

	//bi-directional many-to-one association to BookFile
	@OneToMany(mappedBy="book")
	private List<BookFile> bookFiles;

	public Book() {
	}

	public String getBookId() {
		return this.bookId;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImage() {
		return this.image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getIsbn13() {
		return this.isbn13;
	}

	public void setIsbn13(String isbn13) {
		this.isbn13 = isbn13;
	}

	public BigInteger getPopularity() {
		return this.popularity;
	}

	public void setPopularity(BigInteger popularity) {
		this.popularity = popularity;
	}

	public int getPubYear() {
		return this.pubYear;
	}

	public void setPubYear(int pubYear) {
		this.pubYear = pubYear;
	}

	public String getThumb() {
		return this.thumb;
	}

	public void setThumb(String thumb) {
		this.thumb = thumb;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<Account> getAccounts() {
		return this.accounts;
	}

	public void setAccounts(List<Account> accounts) {
		this.accounts = accounts;
	}

	public List<Author> getAuthors() {
		return this.authors;
	}

	public void setAuthors(List<Author> authors) {
		this.authors = authors;
	}

	public List<Category> getCategories() {
		return this.categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	public List<Publisher> getPublishers() {
		return this.publishers;
	}

	public void setPublishers(List<Publisher> publishers) {
		this.publishers = publishers;
	}

	public List<BookFile> getBookFiles() {
		return this.bookFiles;
	}

	public void setBookFiles(List<BookFile> bookFiles) {
		this.bookFiles = bookFiles;
	}

	public BookFile addBookFile(BookFile bookFile) {
		getBookFiles().add(bookFile);
		bookFile.setBook(this);

		return bookFile;
	}

	public BookFile removeBookFile(BookFile bookFile) {
		getBookFiles().remove(bookFile);
		bookFile.setBook(null);

		return bookFile;
	}

}