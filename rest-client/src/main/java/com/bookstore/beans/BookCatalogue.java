package com.bookstore.beans;

import com.bookstore.domain.Book;
import com.bookstore.restclient.BookService;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

/**
 * Source code github.com/readlearncode
 *
 * @author Renzo T.
 * @version 1.0
 */
@Named
@RequestScoped
public class BookCatalogue {

    @Inject
    private BookService bookService;

    private List<Book> books;

    @PostConstruct
    public void initialize() {
        books = bookService.getBooks();
    }

    public List<Book> getBooks() {
        return books;
    }

}