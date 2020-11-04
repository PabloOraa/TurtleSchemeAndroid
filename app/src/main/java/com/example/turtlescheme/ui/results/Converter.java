package com.example.turtlescheme.ui.results;

import android.os.StrictMode;
import android.util.ArraySet;

import com.example.turtlescheme.Interfaces.OmbdAPI;
import com.example.turtlescheme.Multimedia.Book;
import com.example.turtlescheme.Multimedia.BooksGA.BooksGA;
import com.example.turtlescheme.Multimedia.BooksGA.Item;
import com.example.turtlescheme.Multimedia.Movie;
import com.example.turtlescheme.Multimedia.Multimedia;
import com.example.turtlescheme.Multimedia.Music;
import com.example.turtlescheme.Multimedia.MusicGA.Datum;
import com.example.turtlescheme.Multimedia.MusicGA.MusicGA;
import com.example.turtlescheme.Multimedia.OmbdGA.OmbdGA;
import com.example.turtlescheme.Multimedia.OmbdGA.OmbdGADetails;
import com.example.turtlescheme.Multimedia.OmbdGA.Search;
import com.example.turtlescheme.Multimedia.Serie;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Converter
{
    private static volatile boolean continueToReturn;

    public static List<Book> convertToBookList(BooksGA books)
    {
        List<Book> bookList = new ArrayList<>();
        for(Item item : books.getItems())
        {
            Book newBook = new Book();
            newBook.setId(item.getId());
            newBook.setTitle(item.getVolumeInfo().getTitle());
            newBook.setActors_authors(item.getVolumeInfo().getAuthors());
            newBook.setPublishDate(item.getVolumeInfo().getPublishedDate());
            newBook.setGender(item.getVolumeInfo().getCategories());
            newBook.setLanguage(item.getVolumeInfo().getLanguage());
            if(item.getVolumeInfo().getImageLinks() != null && item.getVolumeInfo().getImageLinks().getThumbnail() != null)
                newBook.setCover(item.getVolumeInfo().getImageLinks().getThumbnail());
            else if(item.getVolumeInfo().getImageLinks() != null && item.getVolumeInfo().getImageLinks().getSmallThumbnail() != null)
                newBook.setCover(item.getVolumeInfo().getImageLinks().getSmallThumbnail());
            else
                newBook.setCover("");
            newBook.setPlot(item.getVolumeInfo().getDescription());
            newBook.setPublisher(item.getVolumeInfo().getPublisher());
            newBook.setType(Multimedia.BOOK);
            bookList.add(newBook);
        }
        return bookList;
    }

    public static List<Music> convertToMusicList(MusicGA music)
    {
        Set<Music> musicList = new ArraySet<>();
        for(Datum data : music.getData()) //NO gender, no publisher
        {
            Music newMusic = new Music();
            newMusic.setId(data.getAlbum().getId().toString());
            newMusic.setTitle(data.getAlbum().getTitle());
            //Integer duration = (data.getAlbum().getTracklist().length()*data.getDuration());
            newMusic.setDuration(Integer.valueOf((data.getDuration()/60)).toString()); //Temporary while we don't access the tracklist to SUM all track's duration.
            newMusic.setActors_authors(new ArrayList<>(Collections.singleton(data.getArtist().getName())));
            newMusic.setCover(data.getAlbum().getCoverXl());
            newMusic.setType(Multimedia.MUSIC);
            newMusic.setUrl(data.getAlbum().getTracklist());
            musicList.add(newMusic);
        }
        return new ArrayList<>(musicList);
    }

    public static List<Serie> convertToSeriesList(OmbdGA serie)
    {
        Set<Serie> serieList = new ArraySet<>();
        for(Search search : serie.getSearch())
        {
            if(!search.getPoster().equalsIgnoreCase("N/A"))
                serieList.add(getSDetails(search.getTitle()));
        }
        return new ArrayList<>(serieList);
    }

    public static List<Movie> convertToMovieList(OmbdGA serie)
    {
        Set<Movie> movieList = new ArraySet<>();
        for(Search search : serie.getSearch())
        {
            if(!search.getPoster().equalsIgnoreCase("N/A"))
                movieList.add(getMDetails(search.getTitle()));
        }
        return new ArrayList<>(movieList);
    }

    private static Serie getSDetails(String title)
    {
        Serie newSerie = new Serie();
        Retrofit query = new Retrofit.Builder().baseUrl("https://www.omdbapi.com").addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                .setLenient()
                .create())).client(new OkHttpClient.Builder().build()).build();
        OmbdAPI apiService = query.create(OmbdAPI.class);
        Call<OmbdGADetails> call = apiService.getMovieSerieDetails(title, Multimedia.SERIEOMBD);
        try
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Response<OmbdGADetails> response = call.execute();
            if (response.isSuccessful()) {
                OmbdGADetails serie = response.body();
                if (serie != null)
                {
                    newSerie.setSeasonNumber(serie.getTotalSeasons());
                    newSerie.setDurationPerEpisode(serie.getRuntime());
                    newSerie.setCountry(serie.getCountry());
                    if (!serie.getDirector().equalsIgnoreCase("N/A"))
                        newSerie.setDirector(serie.getDirector());
                    newSerie.setPlot(serie.getPlot());
                    newSerie.setActors_authors(Arrays.asList(serie.getActors().split(",")));
                    newSerie.setGender(Arrays.asList(serie.getGenre().split(",")));
                    if (serie.getLanguage().contains(Locale.getDefault().getDisplayLanguage()))
                        newSerie.setLanguage(Locale.getDefault().getDisplayLanguage());
                    else
                        newSerie.setLanguage(serie.getLanguage());
                    newSerie.setId(serie.getImdbID());
                    newSerie.setPublishDate(serie.getReleased());
                    newSerie.setType(Multimedia.SERIE);
                    newSerie.setTitle(title);
                    newSerie.setCover(serie.getPoster());
                }
            } else
                System.out.println(response.errorBody());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return newSerie;
    }

    private static Movie getMDetails(String title)
    {
        continueToReturn = false;
        Movie newMovie = new Movie();
        Retrofit query = new Retrofit.Builder().baseUrl("https://www.omdbapi.com").addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                .setLenient()
                .create())).client(new OkHttpClient.Builder().build()).build();
        OmbdAPI apiService = query.create(OmbdAPI.class);
        Call<OmbdGADetails> call = apiService.getMovieSerieDetails(title, Multimedia.MOVIEOMBD);
        try
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Response<OmbdGADetails> response = call.execute();
            if (response.isSuccessful())
            {
                OmbdGADetails serie = response.body();
                if(serie != null)
                {
                    newMovie.setDirector(serie.getDirector());
                    newMovie.setPlot(serie.getPlot());
                    newMovie.setActors_authors(Arrays.asList(serie.getActors().split(",")));
                    newMovie.setGender(Arrays.asList(serie.getGenre().split(",")));
                    if(serie.getLanguage().contains(Locale.getDefault().getDisplayLanguage()))
                        newMovie.setLanguage(Locale.getDefault().getDisplayLanguage());
                    else
                        newMovie.setLanguage(serie.getLanguage());
                    newMovie.setId(serie.getImdbID());
                    newMovie.setPublishDate(serie.getReleased());
                    newMovie.setType(Multimedia.MOVIE);
                    newMovie.setTitle(title);
                    newMovie.setDuration(serie.getRuntime());
                    newMovie.setCover(serie.getPoster());
                    continueToReturn = true;
                }
            }
            else
                System.out.println(response.errorBody());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return newMovie;
    }
}
