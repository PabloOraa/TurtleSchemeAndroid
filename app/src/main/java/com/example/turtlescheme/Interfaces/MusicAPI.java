package com.example.turtlescheme.Interfaces;

import com.example.turtlescheme.Multimedia.MusicGA.MusicGA;
import com.example.turtlescheme.Multimedia.MusicGA.MusicGADetail;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MusicAPI
{
    @GET("search?")
    Call<MusicGA> getMusic(@Query("q") String text);

    //https://www.theaudiodb.com/
    @GET("api/v1/json/1/search.php")
    Call<MusicGA> getArtist(@Query("s") String artist);

    //https://www.theaudiodb.com/
    @GET("api/v1/json/1/album.php")
    Call<MusicGADetail> getAlbums(@Query("i") String idArtist);
}
