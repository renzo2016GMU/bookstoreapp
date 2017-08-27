package com.bookstore.restclient;

import com.bookstore.domain.Author;

import java.util.List;

/**
 * Source code github.com/readlearncode
 *
 * @author Renzo T.
 * @version 1.0
 */
public interface AuthorService {

    List<Author> getAuthors();

    Author getAuthor(String id);

}
