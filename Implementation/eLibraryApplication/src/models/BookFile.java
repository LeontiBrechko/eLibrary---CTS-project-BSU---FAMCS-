package models;

import models.enums.Format;
import models.enums.Language;
import utils.dataValidation.InternalDataValidationException;

import java.io.Serializable;

/**
 * Created by Leonti on 2016-03-07.
 */
public class BookFile implements Serializable {
    private Format format;
    private Language language;
    private String path;

    public BookFile(Format format, Language language, String path)
            throws InternalDataValidationException {
        this.setFormat(format);
        this.setLanguage(language);
        this.setPath(path);
    }

    public Format getFormat() {
        return format;
    }

    public void setFormat(Format format) throws InternalDataValidationException {
        if (format == null) {
            throw new InternalDataValidationException("Invalid file format");
        }
        this.format = format;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) throws InternalDataValidationException {
        if (language == null) {
            throw new InternalDataValidationException("Invalid file language");
        }
        this.language = language;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) throws InternalDataValidationException {
        if (path == null
                || path.trim().equals("")) {
            throw new InternalDataValidationException("Invalid file path");
        }
        this.path = path;
    }
}
