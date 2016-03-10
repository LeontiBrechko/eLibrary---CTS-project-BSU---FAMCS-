package models;

import models.enums.Language;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

/**
 * Created by Leonti on 2016-03-07.
 */
@Entity
public class Test implements Serializable{
    @Id
    @Column(name = "TEST_ID", unique = true, nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String SOME_DATA;

    @ElementCollection(targetClass = Language.class)
    @JoinTable(name = "BOOK_LANGUAGE", joinColumns = @JoinColumn(name = "BOOK_ID"))
    @Enumerated(EnumType.STRING)
    private Collection<Language> languages;

    public Test() {
        SOME_DATA = "";
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSOME_DATA() {
        return SOME_DATA;
    }

    public void setSOME_DATA(String SOME_DATA) {
        this.SOME_DATA = SOME_DATA;
    }
}
