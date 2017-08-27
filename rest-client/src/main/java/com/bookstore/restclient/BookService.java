package com.bookstore.restclient;

import com.bookstore.domain.Author;
import com.bookstore.domain.Book;

import javax.json.JsonArray;
import java.util.List;

/**
 * Source code github.com/readlearncode
 *
 * @author Renzo T.
 * @version 1.0
 */
public interface BookService {
    List<Book> getBooks();

    Book getBook(String id);

    void deleteBook(String isbn);

    Book saveBook(Book book);

    List<Author> extractAuthors(JsonArray authorArray);
}
