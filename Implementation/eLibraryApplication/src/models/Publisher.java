package models;

import utils.dataValidation.DataValidationException;
import utils.dataValidation.DataValidationUtil;

import java.io.Serializable;

/**
 * Created by Leonti on 2016-03-04.
 */
public class Publisher implements Serializable {
    private String name;
    private String country;
    private String city;
    private String state;
    private int streetNumber;
    private String streetName;

    public Publisher(String name, String country, String city,
                     String state, int streetNumber, String streetName)
            throws DataValidationException {
        this.setName(name);
        this.setCountry(country);
        this.setCity(city);
        this.setState(state);
        this.setStreetNumber(streetNumber);
        this.setStreetName(streetName);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws DataValidationException {
        if (name == null
                || name.trim().equals("")
                || DataValidationUtil.xssInjectionCheck(name)) {
            throw new DataValidationException("Please, enter the category description (without < > symbols)");
        }
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) throws DataValidationException {
        if (country == null
                || country.trim().equals("")
                || DataValidationUtil.xssInjectionCheck(country)) {
            throw new DataValidationException("Please, enter the publisher country (without < > symbols)");
        }
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) throws DataValidationException {
        if (city == null
                || city.trim().equals("")
                || DataValidationUtil.xssInjectionCheck(city)) {
            throw new DataValidationException("Please, enter the publisher city (without < > symbols)");
        }
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) throws DataValidationException {
        if (state == null) {
            state = "";
        }
        if (DataValidationUtil.xssInjectionCheck(state)) {
            throw new DataValidationException("Please, enter the publisher state without < > symbols");
        }
        this.state = state;
    }

    public int getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(int streetNumber) throws DataValidationException {
        if (streetNumber < 0) {
            throw new DataValidationException("Please, enter valid publisher street number (grater than 0)");
        }
        this.streetNumber = streetNumber;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) throws DataValidationException {
        if (streetName == null) {
            streetName = "";
        }
        if (DataValidationUtil.xssInjectionCheck(streetName)) {
            throw new DataValidationException("Please, enter the publisher street name without < > symbols");
        }
        this.streetName = streetName;
    }
}
