package com.bookstore.restclient;

import com.bookstore.domain.Author;
import com.bookstore.domain.BookBuilder;
import com.bookstore.domain.LinkResource;
import com.bookstore.domain.Book;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Source code github.com/readlearncode
 *
 * @author Renzo T.
 * @version 1.0
 */
@ApplicationScoped
public class BookServiceImpl implements BookService {

    private static final String API_URL = "http://localhost:8081/rest-server";
    private static final String BOOKS_ENDPOINT = API_URL + "/api/books";

    private List<Book> cachedBooks = new ArrayList<>();

    private Client client;

    @PostConstruct
    public void initialise() {
        client = ClientBuilder.newClient();
    }

    @Override
    public List<Book> getBooks() {
        WebTarget target = client.target(BOOKS_ENDPOINT);
        Response response = target.request(MediaType.APPLICATION_JSON).get();

        cachedBooks.clear();

        JsonArray jsonArray = response.readEntity(JsonArray.class);
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject bookJson = jsonArray.getJsonObject(i);

            List<LinkResource> hyperlinks = extractLinks(bookJson.getJsonArray("links"));
            List<Author> authors = extractAuthors(bookJson.getJsonArray("authors"));

            Book book = new BookBuilder()
                    .setId(bookJson.getString("id"))
                    .setTitle(bookJson.getString("title"))
                    .setDescription(bookJson.getString("description"))
                    .setPrice((float) bookJson.getJsonNumber("price").doubleValue())
                    .setImageFileName(bookJson.getString("imageFileName"))
                    .setAuthors(authors)
                    .setPublished(bookJson.getString("published"))
                    .setLink(bookJson.getString("link"))
                    .setHyperlinks(hyperlinks)
                    .createBook();

            cachedBooks.add(book);
        }

        return Collections.unmodifiableList(cachedBooks);
    }

    @PreDestroy
    private void destroy() {
        client.close();
    }

    @Override
    public Book getBook(String id) {
        WebTarget target = client.target(BOOKS_ENDPOINT + "/" + id);
        Future<Response> bookCall = target.request(MediaType.APPLICATION_JSON).async().get();

        while (!bookCall.isDone()) ;

        Response response = null;
        try {
            response = bookCall.get(60_000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
        }

        JsonObject jsonResponse = response.readEntity(JsonObject.class);
        List<Author> authors = extractAuthors(jsonResponse.getJsonArray("authors"));
        List<LinkResource> hyperlinks = extractLinks(jsonResponse.getJsonArray("links"));

        Book book = new BookBuilder()
                .setId(jsonResponse.getString("id"))
                .setTitle(jsonResponse.getString("title"))
                .setDescription(jsonResponse.getString("description"))
                .setPrice((float) jsonResponse.getJsonNumber("price").doubleValue())
                .setImageFileName(jsonResponse.getString("imageFileName"))
                .setAuthors(authors)
                .setPublished(jsonResponse.getString("published"))
                .setLink(jsonResponse.getString("link"))
                .setHyperlinks(hyperlinks)
                .createBook();

        return book;
    }

    @Override
    public void deleteBook(String isbn) {
        for (Book book : cachedBooks) {
            if (book.getId().equals(isbn)) {
                for (LinkResource linkResource : book.getLinks()) {
                    if (linkResource.getRel().equals("delete")) {
                        String uri = linkResource.getUri();
                        client.target(uri)
                                .request(MediaType.APPLICATION_JSON)
                                .delete();
                        break;
                    }
                }
            }
        }
    }

    @Override
    public Book saveBook(Book book) {
        return null;
    }


    /**
     * Extracts the links from the json object
     *
     * @param linkArray the JSON array that contains the link list
     * @return list of links
     */
    private List<LinkResource> extractLinks(JsonArray linkArray) {

        List<LinkResource> links = new ArrayList<>();

        for (int j = 0; j < linkArray.size(); j++) {
            JsonObject jObject = linkArray.getJsonObject(j);
            String rel = jObject.getString("rel", "");
            String type = jObject.getString("type", "");
            String uri = jObject.getString("uri", "");
            links.add(new LinkResource(rel, type, uri));
        }

        return Collections.unmodifiableList(links);
    }


    /**
     * Extracts the author list form the json object
     *
     * @param authorArray the JSON Array that contains the author list
     * @return list of authors
     */
    public List<Author> extractAuthors(JsonArray authorArray) {
        List<Author> authors = new ArrayList<>();

        for (int j = 0; j < authorArray.size(); j++) {
            JsonObject jObject = authorArray.getJsonObject(j);
            String id = jObject.getString("id", "");
            String firstName = jObject.getString("firstName", "");
            String lastName = jObject.getString("lastName", "");
            String blogURL = jObject.getString("blogURL", "");
            authors.add(new Author(id, firstName, lastName, blogURL));
        }

        return Collections.unmodifiableList(authors);
    }

}
