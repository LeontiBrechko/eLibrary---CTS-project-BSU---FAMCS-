package models;

import java.io.Serializable;

/**
 * Created by Leonti on 2016-03-04.
 */
public class Category implements Serializable {
    private String name;
    private String description;

    public Category() {
        this.setName("");
        this.setDescription("");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
