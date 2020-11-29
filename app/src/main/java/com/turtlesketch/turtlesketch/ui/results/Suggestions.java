package com.turtlesketch.turtlesketch.ui.results;

import android.app.Activity;
import android.content.Intent;

import com.google.gson.GsonBuilder;
import com.turtlesketch.turtlesketch.Database;
import com.turtlesketch.turtlesketch.Interfaces.GoogleAPI;
import com.turtlesketch.turtlesketch.Interfaces.MusicAPI;
import com.turtlesketch.turtlesketch.Interfaces.OmbdAPI;
import com.turtlesketch.turtlesketch.Multimedia.BooksGA.BooksGA;
import com.turtlesketch.turtlesketch.Multimedia.Multimedia;
import com.turtlesketch.turtlesketch.Multimedia.MultimediaSerializable;
import com.turtlesketch.turtlesketch.Multimedia.MusicGA.MusicGA;
import com.turtlesketch.turtlesketch.Multimedia.OmbdGA.OmbdGA;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Suggestions
{
    /**
     * List of the media objects
     */
    List<Multimedia> listMedia;
    /**
     * Activity to do some of the functions.
     */
    Activity act;

    /**
     * Get and show the suggestions based on the content type.
     * @param type Type of the content to get the Suggestions
     *         <br/><ul><li>Books</li><li>Movie</li><li>Music</li><li>Serie</li></ul>
     * @param activity Activity to create the new intent once we have the startActivity.
     * @author Pablo Oraa Lopez
     */
    public void showSuggestions(@NotNull String type, Activity activity)
    {
        act = activity;
        switch (type) {
            case Multimedia.BOOK:
                createSuggestionForBooks();
                break;
            case Multimedia.MOVIE:
                createSuggestionForMovies();
                break;
            case Multimedia.MUSIC:
                createSuggestionForMusic();
                break;
            case Multimedia.SERIE:
                createSuggestionForSeries();
                break;
        }
    }

    /**
     * Create the intent for the List based on the suggestions obtained.
     * @author Pablo Oraa lopez
     */
    private void createIntentForList()
    {
        Intent intent = new Intent(act, ListMedia.class);
        intent.putExtra("media", new MultimediaSerializable(listMedia));
        intent.putExtra("sugg", true);
        act.startActivityForResult(intent,2);
    }

    /**
     * Create the suggestions for the IMBD API based on the user and their current collection.
     * @author Pablo Oraa Lopez
     */
    private void createSuggestionForSeries()
    {
        Retrofit query = createRetrofit("https://www.omdbapi.com");
        OmbdAPI apiService = query.create(OmbdAPI.class);
        Call<OmbdGA> call = apiService.getMovieSerie("", Multimedia.MOVIEOMBD);
        call.enqueue(new Callback<OmbdGA>()
        {
            @Override
            public void onResponse(@NotNull Call<OmbdGA> call, @NotNull Response<OmbdGA> response)
            {
                if (response.isSuccessful())
                {
                    OmbdGA mediaSM = response.body();
                    if (mediaSM != null && mediaSM.getTotalResults() != null && Integer.parseInt(mediaSM.getTotalResults()) > 0)
                        listMedia = new ArrayList<>(Converter.convertToMovieList(mediaSM));

                    if (listMedia != null && listMedia.size() < 0)
                        createIntentForList();
                }
                else
                    System.out.println(response.errorBody());
            }

            @Override
            public void onFailure(@NotNull Call<OmbdGA> call, @NotNull Throwable t)
            {
                t.printStackTrace();
            }
        });
    }

    /**
     * Create the suggestions for the TheAudioDB API based on the user and their current collection.
     * @author Pablo Oraa Lopez
     */
    private void createSuggestionForMusic()
    {
        Retrofit query = createRetrofit("https://www.theaudiodb.com");
        MusicAPI apiService = query.create(MusicAPI.class);
        Call<MusicGA> call = apiService.getArtist("");
        call.enqueue(new Callback<MusicGA>()
        {
            @Override
            public void onResponse(@NotNull Call<MusicGA> call, @NotNull Response<MusicGA> response)
            {
                if (response.isSuccessful())
                {
                    MusicGA mediaSM = response.body();
                    if (mediaSM != null && mediaSM.getArtists() != null && mediaSM.getArtists().size() > 0)
                        listMedia = new ArrayList<>(Converter.convertToMusicList(mediaSM));

                    if (listMedia != null && listMedia.size() < 0)
                        createIntentForList();
                }
                else
                    System.out.println(response.errorBody());
            }

            @Override
            public void onFailure(@NotNull Call<MusicGA> call, @NotNull Throwable t)
            {
                t.printStackTrace();
            }
        });
    }

    /**
     * Create the suggestions for the IMBD API based on the user and their current collection.
     * @author Pablo Oraa Lopez
     */
    private void createSuggestionForMovies()
    {
        Retrofit query = createRetrofit("https://www.omdbapi.com");
        OmbdAPI apiService = query.create(OmbdAPI.class);
        Call<OmbdGA> call = apiService.getMovieSerie("", Multimedia.MOVIEOMBD);
        call.enqueue(new Callback<OmbdGA>()
        {
            @Override
            public void onResponse(@NotNull Call<OmbdGA> call, @NotNull Response<OmbdGA> response)
            {
                if (response.isSuccessful())
                {
                    OmbdGA mediaSM = response.body();
                    if (mediaSM != null && mediaSM.getTotalResults() != null && Integer.parseInt(mediaSM.getTotalResults()) > 0)
                        listMedia = new ArrayList<>(Converter.convertToMovieList(mediaSM));

                    if (listMedia != null && listMedia.size() < 0)
                        createIntentForList();
                }
                else
                    System.out.println(response.errorBody());
            }

            @Override
            public void onFailure(@NotNull Call<OmbdGA> call, @NotNull Throwable t)
            {
                t.printStackTrace();
            }
        });
    }

    /**
     * Create the suggestions for the Google Books API based on the user and their current collection.
     * @author Pablo Oraa Lopez
     */
    private void createSuggestionForBooks()
    {
        Retrofit query = createRetrofit("https://www.googleapis.com");
        GoogleAPI apiService = query.create(GoogleAPI.class);
        String categories = getDatabase().getMostTypicalCategories(getDatabase().getWritableDatabase());
        Call<BooksGA> call = apiService.getBooks("categories:" + categories);
        call.enqueue(new Callback<BooksGA>()
        {
            @Override
            public void onResponse(@NotNull Call<BooksGA> call, @NotNull Response<BooksGA> response)
            {
                if (response.isSuccessful())
                {
                    BooksGA mediaSM = response.body();
                    if (mediaSM != null && mediaSM.getTotalItems() != null && mediaSM.getTotalItems() > 0)
                        listMedia = new ArrayList<>(Converter.convertToBookList(mediaSM));

                    if (listMedia != null && listMedia.size() > 0)
                        createIntentForList();
                }
                else
                    System.out.println(response.errorBody());
            }

            @Override
            public void onFailure(@NotNull Call<BooksGA> call, @NotNull Throwable t)
            {
                t.printStackTrace();
            }
        });
    }

    /**
     * Create the database object based on the current db name.
     * @return Database object.
     * @author Pablo Oraa Lopez
     */
    @NotNull
    @Contract(" -> new")
    private Database getDatabase()
    {
        return new Database(act,"turtlesketch.db", null, 3);
    }

    /**
     * Create the retrofit object to do the call to the third party services.
     * @param url Base URL to get the data from.
     * @return Retrofit object.
     * @author Pablo Oraa Lopez
     */
    @NotNull
    private Retrofit createRetrofit(String url)
    {
        return new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create())).client(new OkHttpClient.Builder().build()).build();
    }
}
