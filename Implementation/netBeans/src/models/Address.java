package models;

import java.io.Serializable;

/**
 * Created by Leonti on 2016-03-04.
 */
public class Address implements Serializable{
    private String country;
    private String city;
    private String state;
    private int streetNumber;
    private String streetName;

    public Address() {
        this.setCountry("");
        this.setCity("");
        this.setState("");
        this.setStreetNumber(0);
        this.setStreetName("");
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

    public int getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(int streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }
}
