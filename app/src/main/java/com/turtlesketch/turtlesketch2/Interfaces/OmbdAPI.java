package com.turtlesketch.turtlesketch2.Interfaces;

import com.turtlesketch.turtlesketch2.Multimedia.OmbdGA.OmbdGA;
import com.turtlesketch.turtlesketch2.Multimedia.OmbdGA.OmbdGADetails;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Interface that contains all the details to get full coverage of the methods.
 */
public interface OmbdAPI
{
    /**
     * Get the movie or series based on the query made by the user.
     * @param text Text to search.
     * @param type Type of the content to search.
     * @return OMBDGA object to store the content received.
     */
    @GET("?apikey=758c9925&r=json")
    Call<OmbdGA> getMovieSerie(@Query("s") String text, @Query("type") String type);

    /**
     * Get the movie or serie details based on the query made by the user and it's selection.
     * @param text Text to search all the details.
     * @param type Type of the content to search the details.
     * @return OMBDGADetails object to store the content received.
     */
    @GET("?apikey=758c9925&r=json&plot=short")
    Call<OmbdGADetails> getMovieSerieDetails(@Query("t") String text, @Query("type") String type);
}
