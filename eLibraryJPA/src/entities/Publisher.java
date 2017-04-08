package entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the publisher database table.
 * 
 */
@Entity
@NamedQuery(name="Publisher.findAll", query="SELECT p FROM Publisher p")
public class Publisher implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="PUB_ID")
	private String pubId;

	private String city;

	private String country;

	private String name;

	private String state;

	@Column(name="STREET_NAME")
	private String streetName;

	@Column(name="STREET_NUM")
	private int streetNum;

	//bi-directional many-to-many association to Book
	@ManyToMany(mappedBy="publishers")
	private List<Book> books;

	public Publisher() {
	}

	public String getPubId() {
		return this.pubId;
	}

	public void setPubId(String pubId) {
		this.pubId = pubId;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getStreetName() {
		return this.streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public int getStreetNum() {
		return this.streetNum;
	}

	public void setStreetNum(int streetNum) {
		this.streetNum = streetNum;
	}

	public List<Book> getBooks() {
		return this.books;
	}

	public void setBooks(List<Book> books) {
		this.books = books;
	}

}