package entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;


/**
 * The persistent class for the publisher database table.
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "Publisher.findAll", query = "SELECT p FROM Publisher p"),
        @NamedQuery(name = "Publisher.findByName", query = "SELECT p FROM Publisher p WHERE p.name = :name")
})
public class Publisher extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "NAME", nullable = false, unique = true)
    private String name;

    @Column(name = "COUNTRY")
    private String country;

    @Column(name = "CITY")
    private String city;

    @Column(name = "STATE")
    private String state;

    @Column(name = "STREET_NAME")
    private String streetName;

    @Column(name = "STREET_NUM")
    private Integer streetNum;

    //bi-directional many-to-many association to Book
    @ManyToMany
    @JoinTable(name = "BookPublisher",
            joinColumns = @JoinColumn(name = "PUB_ID", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "BOOK_ID", nullable = false))
    private List<Book> books;

    public Publisher() {
    }

    public Publisher(String name, String country, String city,
                     String state, int streetNumber, String streetName) {
        this.setName(name);
        this.setCountry(country);
        this.setCity(city);
        this.setState(state);
        this.setStreetNum(streetNumber);
        this.setStreetName(streetName);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public Integer getStreetNum() {
        return streetNum;
    }

    public void setStreetNum(Integer streetNum) {
        this.streetNum = streetNum;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Publisher publisher = (Publisher) o;

        return name != null ? name.equals(publisher.name) : publisher.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}