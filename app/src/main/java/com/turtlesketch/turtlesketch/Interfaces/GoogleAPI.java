package com.turtlesketch.turtlesketch.Interfaces;

import com.turtlesketch.turtlesketch.Multimedia.BooksGA.BooksGA;
import com.turtlesketch.turtlesketch.Multimedia.LibraryGA.LibraryGA;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Interface that contains all the details to get full coverage of the methods.
 */
public interface GoogleAPI
{
    /**
     * Get the books from the query on Google Books without any token.
     * @param text Text to search on Google Books API
     * @return BooksGA with all the data information.
     */
    @GET("books/v1/volumes?key=AIzaSyAbLxPYh7E0khrec6eNQHynvEJ5gJOu0B8")
    Call<BooksGA> getBooks(@Query("q") String text);

    /**
     * Get the libraries from the user on Google Books with token.
     * @param autho Token to be send on the Authorization to get all private data
     * @return LibraryGA with all the data information of it's lists.
     */
    @GET("books/v1/mylibrary/bookshelves?key=AIzaSyAbLxPYh7E0khrec6eNQHynvEJ5gJOu0B8")
    Call<LibraryGA> getLibraries(@Header("Authorization") String autho);

    /**
     * Get the books from the user on Google Books with token.
     * @param autho Token to be send on the Authorization to get all private data
     * @return BooksGA with all the data information of it's lists.
     */
    @GET("books/v1/mylibrary/bookshelves/{shelfID}/volumes?key=AIzaSyAbLxPYh7E0khrec6eNQHynvEJ5gJOu0B8")
    Call<BooksGA> getBooksByLibrary(@Header("Authorization") String autho, @Path("shelfID") String selfID);
}
