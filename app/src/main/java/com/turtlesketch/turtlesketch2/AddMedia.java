package com.turtlesketch.turtlesketch2;

import android.app.Activity;
import android.app.AlertDialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.gson.GsonBuilder;
import com.turtlesketch.turtlesketch2.Interfaces.MySQLAPI;
import com.turtlesketch.turtlesketch2.Multimedia.Book;
import com.turtlesketch.turtlesketch2.Multimedia.Movie;
import com.turtlesketch.turtlesketch2.Multimedia.Multimedia;
import com.turtlesketch.turtlesketch2.Multimedia.Music;
import com.turtlesketch.turtlesketch2.Multimedia.Serie;
import com.turtlesketch.turtlesketch2.R;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Activity to Add media into SQL database and SQLite database when the user creates the media object.
 */
public class AddMedia extends Activity
{
    /**
     * Media object to create and insert the Multimedia.
     */
    Multimedia media;
    /**
     * Type of the media to create.
     */
    String type;

    /**
     * {@inheritDoc}
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_media);
        checkTheme(Config.theme);
    }

    /**
     * {@inheritDoc}
     * <br/>
     * Creates the listener and the type to insert to configure as expected.
     */
    @Override
    protected void onStart()
    {
        super.onStart();
        configTitleType();
        createListener();
    }

    /**
     * Configure the type of the media to be inserted and set the Title to the one is passed on the Intent.
     */
    private void configTitleType()
    {
        type = getIntent().getStringExtra("type");
        if(getIntent().hasExtra("title"))
            ((EditText)findViewById(R.id.tv_media_title_add)).setText(getIntent().getStringExtra("title"));
    }

    /**
     * Create the listener to recover all data inserted by the user and insert into the Database.
     */
    private void createListener()
    {
        findViewById(R.id.bt_add_media_add).setOnClickListener(v ->
        {
            String title = ((EditText)findViewById(R.id.tv_media_title_add)).getText().toString();
            String actorAuthor = ((EditText)findViewById(R.id.tv_media_author_add)).getText().toString();
            String publisherDir = ((EditText)findViewById(R.id.tv_media_publisher_add)).getText().toString();
            String duration = ((EditText)findViewById(R.id.tv_media_duration_add)).getText().toString();
            DatePicker datePicker = findViewById(R.id.tv_media_publishDate_add);
            String publishDate = datePicker.getYear() + "-" + datePicker.getMonth() + "-" + datePicker.getDayOfMonth();
            String plotDesc = ((EditText)findViewById(R.id.tv_media_plot_add)).getText().toString();
            String gender = ((EditText)findViewById(R.id.tv_media_gender_add)).getText().toString();
            String lang = ((EditText)findViewById(R.id.tv_media_language_add)).getText().toString();
            connectAndInsert(title,actorAuthor,publisherDir,duration,publishDate,plotDesc,gender,lang);
        });
    }

    /**
     * Connect to the database based on mySQL using the own webservice to insert the data into that
     * specific type so other users can recover it.
     * @param title Title of the media object.
     * @param actorAuthor Actors/Authors/Artists of the media object.
     * @param publisherDir Publisher/Director of the media object.
     * @param duration Duration of the media object.
     * @param publishDate Date in which the media object has been created.
     * @param plotDesc Plot/Description of the media object.
     * @param gender Gender of the media object
     * @param lang Language of the media Object.
     */
    private void connectAndInsert(String title, String actorAuthor, String publisherDir, String duration, String publishDate, String plotDesc, String gender, String lang)
    {
        if(media == null)
            createMedia(title,actorAuthor,publisherDir,duration,publishDate,plotDesc,gender,lang);
        Call<Integer> call = null;
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://turtlesketch.consulting").addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create())).client(new OkHttpClient.Builder().build()).build();
        MySQLAPI mySQLAPI = retrofit.create(MySQLAPI.class);
        if(type.equalsIgnoreCase(getString(R.string.movie)))
            call = mySQLAPI.insertMovie(title,publisherDir,actorAuthor,plotDesc,gender,"",lang,publishDate,duration);
        else if(type.equalsIgnoreCase(getString(R.string.books)))
            call = mySQLAPI.insertBook(title,actorAuthor,publisherDir,plotDesc,gender,"",lang,publishDate);
        else if(type.equalsIgnoreCase(getString(R.string.music)))
            call = mySQLAPI.insertMusic(title,actorAuthor,publisherDir,plotDesc,gender,"",lang,publishDate,duration,title);
        else if(type.equalsIgnoreCase(getString(R.string.serie)))
            call = mySQLAPI.insertSerie(title,publisherDir,actorAuthor,plotDesc,gender,"",lang,publishDate,duration,"","");

        if(call != null)
        {
            call.enqueue(new Callback<>() {
                @Override
                public void onResponse(@NotNull Call<Integer> call, @NotNull Response<Integer> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null && response.body() == 1) {
                            successMessage();
                        } else
                            errorMessgae();
                    } else {
                        System.out.println(response.errorBody());
                    }
                }

                @Override
                public void onFailure(@NotNull Call<Integer> call, @NotNull Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }

    private void createMedia(String title, String actorAuthor, String publisherDir, String duration, String publishDate, String plotDesc, String gender, String lang)
    {
        if(type.equalsIgnoreCase(getString(R.string.books)))
            media = createBook(title,actorAuthor,publisherDir,publishDate,plotDesc,gender,lang);
        else if(type.equalsIgnoreCase(getString(R.string.music)))
            media = createMusic(title,actorAuthor,publisherDir,duration,publishDate,plotDesc,gender,lang);
        else if(type.equalsIgnoreCase(getString(R.string.movie)))
            media = createMovie(title,actorAuthor,publisherDir,duration,publishDate,plotDesc,gender,lang);
        else if(type.equalsIgnoreCase(getString(R.string.serie)))
            media = createSerie(title, actorAuthor,publisherDir,duration,publishDate,plotDesc,gender,lang, "","");
    }

    @NotNull
    private Multimedia createSerie(String title, @NotNull String actorAuthor, String publisherDir, String duration, String publishDate, String plotDesc, @NotNull String gender, String lang, String seasonNumber, String country)
    {
        Serie serie = new Serie();
        serie.setId(title + "1");
        serie.setType(Multimedia.SERIE);
        serie.setTitle(title);
        serie.setActors_authors(Arrays.asList(actorAuthor.split(",")));
        serie.setPublishDate(publishDate);
        serie.setGender(Arrays.asList(gender.split(",")));
        serie.setLanguage(lang);
        serie.setDurationPerEpisode(duration);
        serie.setDirector(publisherDir);
        serie.setPlot(plotDesc);
        serie.setSeasonNumber(seasonNumber);
        serie.setCountry(country);
        return serie;
    }

    @NotNull
    private Multimedia createMovie(String title, @NotNull String actorAuthor, String publisherDir, String duration, String publishDate, String plotDesc, @NotNull String gender, String lang)
    {
        Movie movie = new Movie();
        movie.setId(title+"1");
        movie.setType(Multimedia.MOVIE);
        movie.setTitle(title);
        movie.setActors_authors(Arrays.asList(actorAuthor.split(",")));
        movie.setPublishDate(publishDate);
        movie.setGender(Arrays.asList(gender.split(",")));
        movie.setLanguage(lang);
        movie.setDuration(duration);
        movie.setPlot(plotDesc);
        movie.setDirector(publisherDir);
        return movie;
    }

    @NotNull
    private Multimedia createMusic(String title, @NotNull String actorAuthor, String publisherDir, String duration, String publishDate, String plotDesc, @NotNull String gender, String lang)
    {
        Music music = new Music();
        music.setId(title+"1");
        music.setType(Multimedia.MUSIC);
        music.setTitle(title);
        music.setActors_authors(Arrays.asList(actorAuthor.split(",")));
        music.setPublishDate(publishDate);
        music.setGender(Arrays.asList(gender.split(",")));
        music.setLanguage(lang);
        music.setPublisher(publisherDir);
        music.setDescription(plotDesc);
        music.setDuration(duration);
        return music;
    }

    @NotNull
    private Multimedia createBook(String title, @NotNull String actorAuthor, String publisherDir, String publishDate, String plotDesc, @NotNull String gender, String lang)
    {
        Book libro = new Book();
        libro.setId(title+"1");
        libro.setType(Multimedia.BOOK);
        libro.setTitle(title);
        libro.setActors_authors(Arrays.asList(actorAuthor.split(",")));
        libro.setPublishDate(publishDate);
        libro.setGender(Arrays.asList(gender.split(",")));
        libro.setLanguage(lang);
        libro.setPlot(plotDesc);
        libro.setPublisher(publisherDir);
        return libro;
    }

    /**
     * Show the error message into one AlertDialog for the error to be inserted.
     */
    private void errorMessgae()
    {
        //Insert NO
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(getText(R.string.error));
        alert.setMessage(getText(R.string.error_insert));
        AlertDialog dialog = alert.create();
        dialog.show();
    }

     /**
      * Show a message that the content has been inserted successfully.
      */
    private void successMessage()
    {
        insertIntoMySQL();
        //Insert OK
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(getText(R.string.confirm));
        alert.setMessage(getText(R.string.confirm_insert));
        alert.setPositiveButton("Ok", (dialog, which) -> finish());
        AlertDialog dialog = alert.create();
        dialog.show();
    }

    /**
     * Method to insert the media object into the SQLite database of the device.
     */
    private void insertIntoMySQL()
    {
        Database db = new Database(this,"turtlesketch2.db", null, 3);//getResources().getStringArray(R.array.sections));
        SQLiteDatabase connection = db.getWritableDatabase();
        db.insertMultimedia(connection, media);//media))
    }

    /**
     * Change the theme of the current Activity to match the system configuration or the selection of the User.
     * <br/><br/>
     * For the automatic configuration, it will depend on the system version we are running the App. If it's Android 10 or newer it will use the option included by Google.
     * In the case is Android 9 (Android Pie) it will use the battery saver to decide the theme of the application.
     * @param selectedText Actual theme selected by the user. If they never change it, auto will be the default option.
     * @author Pablo Oraa Lopez
     */
    private void checkTheme(@NotNull String selectedText)
    {
        if(selectedText.equalsIgnoreCase(getString(R.string.light_theme)))
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        else if(selectedText.equalsIgnoreCase(getString(R.string.dark_theme)))
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else
        if(android.os.Build.VERSION.SDK_INT >= 29)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY);
    }
}
