package models;

import java.io.Serializable;

/**
 * Created by Leonti on 2016-03-04.
 */
public class Publisher implements Serializable {
    private String name;
    private Address address;

    public Publisher() {
        this.setName("");
        this.setAddress(new Address());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
