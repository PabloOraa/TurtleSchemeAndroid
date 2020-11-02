package com.example.turtlescheme.Interfaces;

import com.example.turtlescheme.Multimedia.MusicGA.MusicGA;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DeezerAPI
{
    @GET("search?")
    Call<MusicGA> getMusic(@Query("q") String text);
}
