package com.turtlesketch.turtlesketch.Interfaces;

import com.turtlesketch.turtlesketch.Multimedia.BooksGA.BooksGA;
import com.turtlesketch.turtlesketch.Multimedia.LibraryGA.LibraryGA;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface GoogleAPI
{
    @GET("books/v1/volumes?key=AIzaSyAbLxPYh7E0khrec6eNQHynvEJ5gJOu0B8")
    Call<BooksGA> getBooks(@Query("q") String text);

    @GET("books/v1/mylibrary/bookshelves?key=AIzaSyAbLxPYh7E0khrec6eNQHynvEJ5gJOu0B8")
    Call<LibraryGA> getLibraries(@Header("Authorization") String autho);

    @GET("books/v1/mylibrary/bookshelves/{shelfID}/volumes?key=AIzaSyAbLxPYh7E0khrec6eNQHynvEJ5gJOu0B8")
    Call<BooksGA> getBooksByLibrary(@Header("Authorization") String autho, @Path("shelfID") String selfID);
}
