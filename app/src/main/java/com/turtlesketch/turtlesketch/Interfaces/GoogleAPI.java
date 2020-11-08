package com.turtlesketch.turtlesketch.Interfaces;

import com.turtlesketch.turtlesketch.Multimedia.BooksGA.BooksGA;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface GoogleAPI
{
    @GET("books/v1/volumes?key=AIzaSyAbLxPYh7E0khrec6eNQHynvEJ5gJOu0B8")
    Call<BooksGA> getBooks(@Query("q") String text);
}
