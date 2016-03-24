package models;

import models.enums.Format;
import models.enums.Language;

import java.io.Serializable;

/**
 * Created by Leonti on 2016-03-07.
 */
public class BookFile implements Serializable {
    private Format format;
    private Language language;
    private String path;

    public BookFile() {}

    public Format getFormat() {
        return format;
    }

    public void setFormat(Format format) {
        this.format = format;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
