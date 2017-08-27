package com.bookstore.beans;

import com.bookstore.domain.Author;
import com.bookstore.restclient.AuthorService;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Source code github.com/readlearncode
 *
 * @author Renzo T.
 * @version 1.0
 */
@Named
@RequestScoped
public class AuthorList {

    @Inject
    private AuthorService authorService;

    private List<Author> authors;

    @PostConstruct
    public void initialize() {
        authors = authorService.getAuthors();
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public Map<String, String> getAuthorsAsMap() {
        return getAuthors()
                .stream()
                .collect(Collectors.toMap(o -> o.getId(), o -> o.getFirstName() + " " + o.getLastName()));
    }

}