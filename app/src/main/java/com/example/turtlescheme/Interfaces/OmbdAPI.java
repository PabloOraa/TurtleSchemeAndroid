package com.example.turtlescheme.Interfaces;

import com.example.turtlescheme.Multimedia.BooksGA.BooksGA;
import com.example.turtlescheme.Multimedia.OmbdGA.OmbdGA;
import com.example.turtlescheme.Multimedia.OmbdGA.OmbdGADetails;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OmbdAPI
{
    @GET("?apikey=758c9925&r=json")
    Call<OmbdGA> getMovieSerie(@Query("s") String text, @Query("type") String type);

    @GET("?apikey=758c9925&r=json&plot=short")
    Call<OmbdGADetails> getMovieSerieDetails(@Query("t") String text, @Query("type") String type);
}
