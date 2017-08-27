package com.bookstore.restserver.infrastructure;

import com.bookstore.restserver.domain.Book;

import java.util.List;
import java.util.Optional;

/**
 * @author Renzo T.
 * @version 1.0
 */
public interface BookRepository {

    Book saveBook(final Book book);

    Optional<Book> deleteBook(final String id);

    List<Book> getAll();

    Optional<Book> getByISBN(String isbn);

}