package com.turtlesketch.turtlesketch2.ui.results;

import android.os.StrictMode;
import android.util.ArraySet;

import com.turtlesketch.turtlesketch2.Interfaces.MusicAPI;
import com.turtlesketch.turtlesketch2.Interfaces.OmbdAPI;
import com.turtlesketch.turtlesketch2.Multimedia.Book;
import com.turtlesketch.turtlesketch2.Multimedia.BooksGA.BooksGA;
import com.turtlesketch.turtlesketch2.Multimedia.BooksGA.Item;
import com.turtlesketch.turtlesketch2.Multimedia.MYSQL.BooksGA.Result;
import com.turtlesketch.turtlesketch2.Multimedia.MYSQL.MovieGA.MovieGA;
import com.turtlesketch.turtlesketch2.Multimedia.MYSQL.SerieGA.SerieGA;
import com.turtlesketch.turtlesketch2.Multimedia.Movie;
import com.turtlesketch.turtlesketch2.Multimedia.Multimedia;
import com.turtlesketch.turtlesketch2.Multimedia.Music;
import com.turtlesketch.turtlesketch2.Multimedia.MusicGA.Album;
import com.turtlesketch.turtlesketch2.Multimedia.MusicGA.Artist;
import com.turtlesketch.turtlesketch2.Multimedia.MusicGA.MusicGA;
import com.turtlesketch.turtlesketch2.Multimedia.MusicGA.MusicGADetail;
import com.turtlesketch.turtlesketch2.Multimedia.OmbdGA.OmbdGA;
import com.turtlesketch.turtlesketch2.Multimedia.OmbdGA.OmbdGADetails;
import com.turtlesketch.turtlesketch2.Multimedia.OmbdGA.Search;
import com.turtlesketch.turtlesketch2.Multimedia.Serie;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

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

/**
 * Converter class to change the result of the queries to objects know by the application and SQLiteDB
 */
public class Converter
{

    /**
     * Convert the books received by Google to the Book class known by the app.
     * @param books books received from Google Books API.
     * @return List of Books.
     */
    @NotNull
    public static List<Book> convertToBookList(@NotNull BooksGA books)
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

    /**
     * Convert the songs received by TheAudioDB to the Music class known by the app.
     * @param music Songs received from TheAudioDB API.
     * @return List of songs (Music).
     */
    @NotNull
    @Contract("_ -> new")
    public static List<Music> convertToMusicList(@NotNull MusicGA music)
    {
        Set<Music> musicList = new ArraySet<>();
        for(Artist data : music.getArtists()) //NO gender, no publisher
            musicList = getMusicDetail(data.getIdArtist());

        return new ArrayList<>(musicList);
    }

    /**
     * Convert the series received by IMBD to the Serie class known by the app.
     * @param serie Series received from IMBD API.
     * @return List of series.
     */
    @NotNull
    @Contract("_ -> new")
    public static List<Serie> convertToSeriesList(@NotNull OmbdGA serie)
    {
        Set<Serie> serieList = new ArraySet<>();
        for(Search search : serie.getSearch())
        {
            if(!search.getPoster().equalsIgnoreCase("N/A"))
                serieList.add(getSDetails(search.getTitle()));
        }
        return new ArrayList<>(serieList);
    }

    /**
     * Convert the movies received by IMBD to the Movie class known by the app.
     * @param movie Movies received from IMBD API.
     * @return List of movies.
     */
    @NotNull
    @Contract("_ -> new")
    public static List<Movie> convertToMovieList(@NotNull OmbdGA movie)
    {
        Set<Movie> movieList = new ArraySet<>();
        for(Search search : movie.getSearch())
        {
            if(!search.getPoster().equalsIgnoreCase("N/A"))
                movieList.add(getMDetails(search.getTitle()));
        }
        return new ArrayList<>(movieList);
    }

    /**
     * Convert the books received by own DB to the Book class known by the app.
     * @param book books received from MySQL DB.
     * @return List of Books.
     */
    @NotNull
    @Contract("_ -> new")
    public static List<Book> convertToBookSQLList(@NotNull com.turtlesketch.turtlesketch2.Multimedia.MYSQL.BooksGA.BooksGA book)
    {
        Set<Book> bookList = new ArraySet<>();
        for(Result search : book.getResults())
        {
            if(search.getAuthor() != null)
            {
                Book newBook = new Book();
                newBook.setId(search.getId());
                newBook.setTitle(search.getTitle());
                newBook.setPublisher(search.getPublisher());
                newBook.setActors_authors(Arrays.asList(search.getAuthor().split(",")));
                newBook.setPlot(search.getPlot());
                newBook.setCover(search.getCover());
                newBook.setGender(Arrays.asList(search.getCategory().split(",")));
                newBook.setLanguage(search.getLang());
                newBook.setPublishDate(search.getPublishDate());
                newBook.setType(Multimedia.BOOK);
                bookList.add(newBook);
            }
        }
        return new ArrayList<>(bookList);
    }

    /**
     * Convert the series received by own DB to the Serie class known by the app.
     * @param serie Series received from MySQL DB.
     * @return List of series.
     */
    @NotNull
    @Contract("_ -> new")
    public static List<Serie> convertToSerieSQLList(@NotNull SerieGA serie)
    {
        Set<Serie> serieList = new ArraySet<>();
        for(com.turtlesketch.turtlesketch2.Multimedia.MYSQL.SerieGA.Result search : serie.getResults())
        {
            if(search.getDurationPerEpisode() != null && search.getPublishDate() != null && search.getGender() != null)
            {
                Serie newSerie = new Serie();
                newSerie.setId(search.getId());
                newSerie.setTitle(search.getTitle());
                newSerie.setDirector(search.getDirector());
                newSerie.setActors_authors(Arrays.asList(search.getActors().split(",")));
                newSerie.setPlot(search.getPlot());
                newSerie.setDurationPerEpisode(search.getDurationPerEpisode());
                newSerie.setCover(search.getCover());
                newSerie.setGender(Arrays.asList(search.getGender().split(",")));
                newSerie.setLanguage(search.getLang());
                newSerie.setPublishDate(search.getPublishDate());
                newSerie.setType(Multimedia.SERIE);
                serieList.add(newSerie);
            }
        }
        return new ArrayList<>(serieList);
    }

    /**
     * Convert the series received by own DB to the Movie class known by the app.
     * @param movie Movies received from MySQL DB.
     * @return List of movies.
     */
    @NotNull
    @Contract("_ -> new")
    public static List<Movie> convertToMovieSQLList(@NotNull MovieGA movie)
    {
        Set<Movie> movieList = new ArraySet<>();
        for(com.turtlesketch.turtlesketch2.Multimedia.MYSQL.MovieGA.Result search : movie.getResults())
        {
            if(search.getDuration() != null && search.getPublishDate() != null && search.getGender() != null)
            {
                Movie newMovie = new Movie();
                newMovie.setId(search.getId());
                newMovie.setTitle(search.getTitle());
                newMovie.setDirector(search.getDirector());
                newMovie.setActors_authors(Arrays.asList(search.getActors().split(",")));
                newMovie.setPlot(search.getPlot());
                newMovie.setDuration(search.getDuration());
                newMovie.setCover(search.getCover());
                newMovie.setGender(Arrays.asList(search.getGender().split(",")));
                newMovie.setLanguage(search.getLang());
                newMovie.setPublishDate(search.getPublishDate());
                newMovie.setType(Multimedia.MOVIE);
                movieList.add(newMovie);
            }
        }
        return new ArrayList<>(movieList);
    }

    /**
     * Convert the songs received by own DB to the Music class known by the app.
     * @param music Songs (Music) received from MySQL DB.
     * @return List of music objects.
     */
    @NotNull
    @Contract("_ -> new")
    public static List<Music> convertToMusicSQLList(@NotNull com.turtlesketch.turtlesketch2.Multimedia.MYSQL.MusicGA.MusicGA music)
    {
        Set<Music> musicList = new ArraySet<>();
        for(com.turtlesketch.turtlesketch2.Multimedia.MYSQL.MusicGA.Result search : music.getResults())
        {
            if(search.getDuration() != null && search.getPublishDate() != null && search.getGender() != null)
            {
                Music newMusic = new Music();
                newMusic.setId(search.getId());
                newMusic.setTitle(search.getTitle());
                newMusic.setPublisher(search.getPublisher());
                newMusic.setActors_authors(Arrays.asList(search.getArtist().split(",")));
                newMusic.setDescription(search.getDescription());
                newMusic.setDuration(search.getDuration());
                newMusic.setCover(search.getCover());
                newMusic.setGender(Arrays.asList(search.getGender().split(",")));
                newMusic.setLanguage(search.getLang());
                newMusic.setPublishDate(search.getPublishDate());
                newMusic.setType(Multimedia.MUSIC);
                musicList.add(newMusic);
            }
        }
        return new ArrayList<>(musicList);
    }

    /**
     * Get all the details of the song get by the search done by the user
     * @param idArtist id of the Artist to get the data.
     * @return Set of Music objects with all the data.
     */
    @NotNull
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

    /**
     * Get all the details of the serie get by the search done by the user
     * @param title Title of the Serie to search all the details.
     * @return Serie object with all the details.
     */
    @NotNull
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

    /**
     * Get all the details of the movie get by the search done by the user
     * @param title Title of the movie to search all the details.
     * @return Movie object with all the details.
     */
    @NotNull
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
