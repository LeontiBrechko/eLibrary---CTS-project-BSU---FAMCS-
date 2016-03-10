package models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Leonti on 2016-03-10.
 */
public class DownloadList implements Serializable {
    private List<Book> bookList;

    public DownloadList() {
    }

    public List<Book> getBookList() {
        return bookList;
    }

    public void setBookList(List<Book> bookList) {
        this.bookList = bookList;
    }
}
