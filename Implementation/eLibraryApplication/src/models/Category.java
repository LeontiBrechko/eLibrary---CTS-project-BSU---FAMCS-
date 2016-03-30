package models;

import utils.dataValidation.DataValidationException;
import utils.dataValidation.DataValidationUtil;

import java.io.Serializable;

/**
 * Created by Leonti on 2016-03-04.
 */
public class Category implements Serializable {
    private String name;
    private String description;

    public Category(String name, String description) throws DataValidationException {
        this.setName(name);
        this.setDescription(description);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws DataValidationException {
        if (name == null
                || name.trim().equals("")
                || DataValidationUtil.xssInjectionCheck(name)) {
            throw new DataValidationException("Please, enter the category name (without < > symbols)");
        }
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) throws DataValidationException {
        if (description == null) {
            description = "";
        }
        if (DataValidationUtil.xssInjectionCheck(name)) {
            throw new DataValidationException("Please, enter the category description (without < > symbols)");
        }
        this.description = description;
    }
}
