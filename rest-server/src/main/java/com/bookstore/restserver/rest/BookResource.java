package com.bookstore.restserver.rest;

import com.bookstore.restserver.domain.LinkResource;
import com.bookstore.restserver.infrastructure.BookRepository;
import com.bookstore.restserver.domain.Book;
import com.bookstore.restserver.infrastructure.exceptions.ISBNNotFoundException;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.Optional;

/**
 * @author Renzo T.
 * @version 1.0
 */
@Stateless
@Path("/books")
public class BookResource {

    @EJB
    private BookRepository bookRepository;

    @Context
    private UriInfo uriInfo;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllBook() {
        List<Book> books = bookRepository.getAll();
        for (Book book : books) {
            Link self = Link.fromUri(uriInfo.getBaseUriBuilder()
                    .path(getClass())
                    .path(getClass(), "getBookByIsbn")
                    .build(book.getId()))
                    .rel("self")
                    .type("GET")
                    .build();

            Link delete = Link.fromUri(uriInfo.getBaseUriBuilder()
                    .path(getClass())
                    .path(getClass(), "deleteBook")
                    .build(book.getId()))
                    .rel("delete")
                    .type("DELETE")
                    .build();

            LinkResource selfLink = new LinkResource(self);
            LinkResource deleteLink = new LinkResource(delete);

            book.addLink(selfLink);
            book.addLink(deleteLink);
        }
        GenericEntity<List<Book>> bookWrapper = new GenericEntity<List<Book>>(books) {
        };
        return Response.ok(bookWrapper).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveBook(@Valid final Book book) {
        Book persistedBook = bookRepository.saveBook(book);
        return Response.status(Response.Status.OK).entity(persistedBook).build();
    }

    @GET
    @Path("{isbn: \\d{9}[\\d|X]$}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBookByIsbn(final @PathParam("isbn") String isbn) throws ISBNNotFoundException {
        Optional<Book> book = bookRepository.getByISBN(isbn);
        if (book.isPresent()) {

            Link self = Link.fromUri(uriInfo.getBaseUriBuilder()
                    .path(getClass())
                    .path(getClass(), "getBookByIsbn")
                    .build(book.get().getId()))
                    .rel("self")
                    .type("GET")
                    .build();

            Link delete = Link.fromUri(uriInfo.getBaseUriBuilder()
                    .path(getClass())
                    .path(getClass(), "deleteBook")
                    .build(book.get().getId()))
                    .rel("delete")
                    .type("DELETE")
                    .build();

            LinkResource selfLink = new LinkResource(self);
            LinkResource deleteLink = new LinkResource(delete);

            book.get().addLink(selfLink);
            book.get().addLink(deleteLink);

            return Response.ok(book.get()).links(self, delete).build();
        }
        throw new ISBNNotFoundException();
    }


    @DELETE
    @Path("{isbn: \\d{9}[\\d|X]$}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteBook(final @PathParam("isbn") String isbn) throws ISBNNotFoundException {
        return Response.ok(bookRepository
                .deleteBook(isbn)
                .orElseThrow(ISBNNotFoundException::new))
                .build();
    }

}
