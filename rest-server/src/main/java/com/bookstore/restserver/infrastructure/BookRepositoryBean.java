package com.bookstore.restserver.infrastructure;


import com.bookstore.restserver.domain.Book;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.*;

/**
 * @author Renzo T.
 * @version 1.0
 */
@Stateless
public class BookRepositoryBean implements BookRepository {

    private static final String IMAGE_LOCATION = "/images/covers/";
    private final Map<String, Book> books = new HashMap<>();
    @EJB
    private AuthorRepository authorRepository;

    @Override
    public Book saveBook(final Book book) {
        if (book.getImageFileName().length() == 0) {
            book.setImageFileName(IMAGE_LOCATION.concat("no_image.png"));
        }
        authorRepository.saveAuthors(book.getAuthors());
        books.put(book.getId(), book);
        return book;
    }

    @Override
    public Optional<Book> deleteBook(final String id) {
        return Optional.of(books.remove(id));
    }

    @Override
    public List<Book> getAll() {
        return new ArrayList<>(books.values());
    }

    @Override
    public Optional<Book> getByISBN(final String id) {
        return Optional.ofNullable(books.get(id));
    }
}