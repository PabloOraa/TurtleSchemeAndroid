package com.turtlesketch.turtlesketch.Interfaces;

import com.turtlesketch.turtlesketch.Multimedia.MYSQL.BooksGA.BooksGA;
import com.turtlesketch.turtlesketch.Multimedia.MYSQL.MovieGA.MovieGA;
import com.turtlesketch.turtlesketch.Multimedia.MYSQL.MusicGA.MusicGA;
import com.turtlesketch.turtlesketch.Multimedia.MYSQL.SerieGA.SerieGA;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MySQLAPI
{
    @GET("androidsql/sqlRequest.php?type=Insert&table=BOOKS")
    Call<Integer> insertBook(@Query("title") String title, @Query("author") String author, @Query("publisher") String publisher, @Query("plot") String plot, @Query("category") String category, @Query("cover") String cover, @Query("lang") String lang, @Query("publishDate") String publishDate);

    @GET("androidsql/sqlRequest.php?type=Insert&table=MUSIC")
    Call<Integer> insertMusic(@Query("title") String title, @Query("artist") String artist, @Query("publisher") String publisher, @Query("description") String description, @Query("gender") String gender, @Query("cover") String cover, @Query("lang") String lang, @Query("publishDate") String publishDate, @Query("duration") String duration, @Query("album") String album);

    @GET("androidsql/sqlRequest.php?type=Insert&table=SERIE")
    Call<Integer> insertSerie(@Query("title") String title, @Query("director") String director, @Query("actors") String actors, @Query("plot") String plot, @Query("gender") String gender, @Query("cover") String cover, @Query("lang") String lang, @Query("publishDate") String publishDate, @Query("durationPerEpisode") String durationPerEpisode, @Query("country") String country, @Query("seasonNumber") String seasonNumber);

    @GET("androidsql/sqlRequest.php?type=Insert&table=MOVIE")
    Call<Integer> insertMovie(@Query("title") String title, @Query("director") String director, @Query("actors") String actors, @Query("plot") String plot, @Query("gender") String gender, @Query("cover") String cover, @Query("lang") String lang, @Query("publishDate") String publishDate, @Query("duration") String duration);


    //WORK IN PROGRESS
    @GET("androidsql/sqlRequest.php?type=Select&table=BOOKS")
    Call<BooksGA> getBook(@Query("title") String title);

    @GET("androidsql/sqlRequest.php?type=Select&table=MUSIC")
    Call<MusicGA> getMusic(@Query("title") String title);

    @GET("androidsql/sqlRequest.php?type=Select&table=SERIE")
    Call<SerieGA> getSerie(@Query("title") String title);

    @GET("androidsql/sqlRequest.php?type=Select&table=MOVIE")
    Call<MovieGA> getMovie(@Query("title") String title);
}
