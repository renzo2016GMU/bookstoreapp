package com.bookstore.beans;

import com.bookstore.domain.Author;
import com.bookstore.restclient.AuthorService;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Source code github.com/readlearncode
 *
 * @author Renzo T.
 * @version 1.0
 */
@Named
@ViewScoped
public class AuthorManager implements Serializable {

    private static final long serialVersionUID = 1;

    @Inject
    private AuthorService authorService;

    private String id;

    private Author author;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Author getAuthor() {
        return author;
    }

    public void onLoad() {
        author = authorService.getAuthor(id);
    }


//    public String submit(Book book) {
//        book = bookService.saveBook(book);
//        return "book-details?id=" + book.getId();
//    }

}