package com.bookstore.restclient;

import com.bookstore.domain.Book;

import javax.ws.rs.client.InvocationCallback;
import java.util.ArrayList;

/**
 * Source code github.com/readlearncode
 *
 * @author Renzo T.
 * @version 1.0
 */
public class BookCallback implements InvocationCallback<ArrayList<Book>> {

    @Override
    public void completed(ArrayList<Book> books) {

    }

    @Override
    public void failed(Throwable throwable) {

    }
}