package com.turtlesketch.turtlesketch.ui.results;

import android.os.StrictMode;
import android.util.ArraySet;

import com.turtlesketch.turtlesketch.Interfaces.MusicAPI;
import com.turtlesketch.turtlesketch.Interfaces.OmbdAPI;
import com.turtlesketch.turtlesketch.Multimedia.Book;
import com.turtlesketch.turtlesketch.Multimedia.BooksGA.BooksGA;
import com.turtlesketch.turtlesketch.Multimedia.BooksGA.Item;
import com.turtlesketch.turtlesketch.Multimedia.Movie;
import com.turtlesketch.turtlesketch.Multimedia.Multimedia;
import com.turtlesketch.turtlesketch.Multimedia.Music;
import com.turtlesketch.turtlesketch.Multimedia.MusicGA.Album;
import com.turtlesketch.turtlesketch.Multimedia.MusicGA.Artist;
import com.turtlesketch.turtlesketch.Multimedia.MusicGA.MusicGA;
import com.turtlesketch.turtlesketch.Multimedia.MusicGA.MusicGADetail;
import com.turtlesketch.turtlesketch.Multimedia.OmbdGA.OmbdGA;
import com.turtlesketch.turtlesketch.Multimedia.OmbdGA.OmbdGADetails;
import com.turtlesketch.turtlesketch.Multimedia.OmbdGA.Search;
import com.turtlesketch.turtlesketch.Multimedia.Serie;
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

    public static List<Book> convertToBookList(BooksGA books)
    {
        List<Book> bookList = new ArrayList<>();
        for(Item item : books.getItems())
        {
            if(!item.getVolumeInfo().getAuthors().isEmpty() && !item.getVolumeInfo().getTitle().isEmpty())
            {
                Book newBook = new Book();
                newBook.setId(item.getId());
                newBook.setTitle(item.getVolumeInfo().getTitle());
                newBook.setActors_authors(item.getVolumeInfo().getAuthors());
                newBook.setPublishDate(item.getVolumeInfo().getPublishedDate());
                newBook.setGender(item.getVolumeInfo().getCategories());
                newBook.setLanguage(item.getVolumeInfo().getLanguage());
                if (item.getVolumeInfo().getImageLinks() != null && item.getVolumeInfo().getImageLinks().getThumbnail() != null)
                    newBook.setCover(item.getVolumeInfo().getImageLinks().getThumbnail());
                else if (item.getVolumeInfo().getImageLinks() != null && item.getVolumeInfo().getImageLinks().getSmallThumbnail() != null)
                    newBook.setCover(item.getVolumeInfo().getImageLinks().getSmallThumbnail());
                else
                    newBook.setCover("");
                newBook.setPlot(item.getVolumeInfo().getDescription());
                newBook.setPublisher(item.getVolumeInfo().getPublisher());
                newBook.setType(Multimedia.BOOK);
                bookList.add(newBook);
            }
        }
        return bookList;
    }

    public static List<Music> convertToMusicList(MusicGA music)
    {
        Set<Music> musicList = new ArraySet<>();
        for(Artist data : music.getArtists()) //NO gender, no publisher
            musicList = getMusicDetail(data.getIdArtist());

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


    private static Set<Music> getMusicDetail(String idArtist)
    {
        Set<Music> musicList = new ArraySet<>();
        Retrofit query = new Retrofit.Builder().baseUrl("https://www.theaudiodb.com").addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
            .setLenient()
            .create())).client(new OkHttpClient.Builder().build()).build();
        MusicAPI apiService = query.create(MusicAPI.class);
        Call<MusicGADetail> call = apiService.getAlbums(idArtist);
        try
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Response<MusicGADetail> response = call.execute();
            if(response.isSuccessful())
            {
                MusicGADetail music = response.body();
                if(music != null)
                {
                    for(Album album : music.getAlbum())
                    {
                        if(!album.getStrArtist().isEmpty() && !album.getStrAlbum().isEmpty())
                        {
                            Music newMusic = new Music();
                            newMusic.setId(album.getIdAlbum());
                            newMusic.setTitle(album.getStrAlbum());
                            newMusic.setLanguage("English");
                            //Integer duration = (data.getAlbum().getTracklist().length()*data.getDuration());
                            newMusic.setDuration("");
                            newMusic.setPublishDate(album.getIntYearReleased());
                            newMusic.setPublisher(album.getStrLabel());
                            newMusic.setGender(Arrays.asList(album.getStrGenre().split(",")));
                            newMusic.setActors_authors(new ArrayList<>(Collections.singleton(album.getStrArtist())));
                            newMusic.setDescription(album.getStrDescriptionEN());
                            newMusic.setCover(album.getStrAlbumThumb());
                            newMusic.setType(Multimedia.MUSIC);
                            newMusic.setUrl("");//data.getAlbum().getTracklist());
                            if (newMusic.getCover() != null)
                                musicList.add(newMusic);
                        }
                    }
                }
            }
            else
                System.out.println(response.errorBody());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return musicList;
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
                if (serie != null && !serie.getTitle().isEmpty())
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
                OmbdGADetails movie = response.body();
                if(movie != null && !movie.getTitle().isEmpty() && !movie.getRuntime().isEmpty() && !movie.getGenre().isEmpty())
                {
                    newMovie.setDirector(movie.getDirector());
                    newMovie.setPlot(movie.getPlot());
                    newMovie.setActors_authors(Arrays.asList(movie.getActors().split(",")));
                    newMovie.setGender(Arrays.asList(movie.getGenre().split(",")));
                    if(movie.getLanguage().contains(Locale.getDefault().getDisplayLanguage()))
                        newMovie.setLanguage(Locale.getDefault().getDisplayLanguage());
                    else
                        newMovie.setLanguage(movie.getLanguage());
                    newMovie.setId(movie.getImdbID());
                    newMovie.setPublishDate(movie.getReleased());
                    newMovie.setType(Multimedia.MOVIE);
                    newMovie.setTitle(title);
                    newMovie.setDuration(movie.getRuntime());
                    newMovie.setCover(movie.getPoster());
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
