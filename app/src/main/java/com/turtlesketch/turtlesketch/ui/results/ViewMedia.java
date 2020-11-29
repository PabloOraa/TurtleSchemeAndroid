package com.turtlesketch.turtlesketch.ui.results;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.turtlesketch.turtlesketch.Config;
import com.turtlesketch.turtlesketch.Database;
import com.turtlesketch.turtlesketch.Multimedia.Book;
import com.turtlesketch.turtlesketch.Multimedia.Movie;
import com.turtlesketch.turtlesketch.Multimedia.Multimedia;
import com.turtlesketch.turtlesketch.Multimedia.Music;
import com.turtlesketch.turtlesketch.Multimedia.Serie;
import com.turtlesketch.turtlesketch.R;
import com.microsoft.device.dualscreen.core.manager.SurfaceDuoScreenManager;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.URL;

/**
 * Activity to show the media that we want it.
 */
public class ViewMedia extends AppCompatActivity
{
    /**
     * Manage the behaviour of the application depending on the state of the Surface Duo
     */
    private SurfaceDuoScreenManager surfaceDuoScreenManager;
    /**
     * Media object that could be stored into the database or it's already stored on the db.
     */
    Multimedia media;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_media);
        changeTheme(Config.theme);
    }

    /**
     * {@inheritDoc}
     * <br/>
     * Once the activity has started it will complete the data with the media object, set the Title
     * and all the correct configuration with the Database.
     */
    @Override
    protected void onStart()
    {
        super.onStart();
        fillMedia();
        setTitle(media.getTitle());
        Database db = new Database(this,"turtlesketch.db", null, 3);
        if(!db.existsMultimedia(db.getReadableDatabase(),media.getTitle()))
            createListener();
        else
            findViewById(R.id.bt_add_media).setVisibility(View.INVISIBLE);
    }

    /**
     * {@inheritDoc}
     * <br/>
     * Set the result if we don't press Add button and do it with the back button/back gesture.
     */
    @Override
    public void onBackPressed()
    {
        setResult(1);
        super.onBackPressed();
    }

    /**
     * Creates the listener for the Add button so it will try to insert the media object into
     * the database and show a message with the result of the Insert.
     */
    private void createListener()
    {
        findViewById(R.id.bt_add_media).setOnClickListener(v ->
        {
            Database db = new Database(this,"turtlesketch.db", null, 3);//getResources().getStringArray(R.array.sections));
            SQLiteDatabase connection = db.getWritableDatabase();
            if(db.insertMultimedia(connection, media))
                //Insert OK
                insertDone();
            else
                //Insert NO
                errorMessage();
        });
    }

    /**
     * Show a message that the content has been inserted successfully. Once the user has press OK,
     * this will dismiss and the Activity will be finished to go back to the Main Screen or to show
     * Suggestions, depending on the type and other conditions.
     */
    private void insertDone()
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(getText(R.string.confirm));
        alert.setMessage(getText(R.string.confirm_insert));
        alert.setPositiveButton("Ok", (dialog, which) ->
            {
                dialog.dismiss();
                setResult(3);
                finish();
            });
        AlertDialog dialog = alert.create();
        dialog.show();
    }

    /**
     * Show the error message into one AlertDialog for the error to be inserted.
     */
    private void errorMessage()
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(getText(R.string.error));
        alert.setMessage(getText(R.string.error_insert));
        AlertDialog dialog = alert.create();
        dialog.show();
    }

    /**
     * Recovers all the content that was passed into the intent and fill the Activity fields with
     * the information that is on the Object from the database/query.
     */
    private void fillMedia()
    {
        Intent getIntent = getIntent();
        media = (Multimedia) getIntent.getSerializableExtra("media");

        fillImage();
        fillAuthorActor();
        ((TextView)findViewById(R.id.tv_media_title)).setText(media.getTitle());
        fillLanguage();
        fillURL();
        fillGender();

        ((TextView)findViewById(R.id.tv_media_publishDate)).setText(media.getPublishDate());
        fillByType();
    }

    /**
     * If there is any image url stored into the media object it will create the bitmap image and
     * show it into the Bitmap section of the Activity.
     */
    private void fillImage()
    {
        if(media.getCover() != null)
            ((ImageView)findViewById(R.id.im_media_cover)).setImageBitmap(getBitmapFromURL(media.getCover()));
    }

    /**
     * If there is any author/actor/artist stored into the Database or into the media object that
     * was found in the search, it will show it in the Activity.
     * <br/>
     * There are three possible ways that the author/actor/artist is shown.
     * <ul>
     *     <li>It could have [ and ] at the beginning and the end, that both will be deleted and only show the text. </li>
     *     <li>It could have [ at the beginning but not at the end, so only will be deleted the first one.</li>
     *     <li>It could have only plain text with no [ or ] so it will be shown as is stored.</li>
     * </ul>
     * <br/><br/>
     * The main purpose of this function is to show <b> author/actor/artist without [ or ]</b>.
     */
    private void fillAuthorActor()
    {
        if(media.getActors_authors() != null)
            if(!media.getActors_authors().get(0).equalsIgnoreCase(""))
                if(media.getActors_authors().toString().contains("["))
                    if(media.getActors_authors().toString().contains("]"))
                        ((TextView)findViewById(R.id.tv_media_author)).setText(media.getActors_authors().toString().split("\\[")[1].split("]")[0]);
                    else
                        ((TextView)findViewById(R.id.tv_media_author)).setText(media.getActors_authors().toString().split("\\[")[1]);
                else
                    ((TextView)findViewById(R.id.tv_media_author)).setText(media.getActors_authors().toString());
    }

    /**
     * If there is any language stored into the media object it will show it in the Activity.
     */
    private void fillLanguage()
    {
        if(media.getLanguage()!=null)
            ((TextView)findViewById(R.id.tv_media_language)).setText(media.getLanguage());
    }

    /**
     * If there is any URL stored into the media object it will show it in the Activity.
     */
    private void fillURL()
    {
        if(media.getUrl() != null)
            ((TextView)findViewById(R.id.tv_media_directorAlbum)).setText(media.getUrl());
    }

    /**
     * If there is any gender stored into the Database or into the media object that was found
     * in the search, it will show it in the Activity.
     * <br/>
     * There are three possible ways that the gender is shown.
     * <ul>
     *     <li>It could have [ and ] at the beginning and the end, that both will be deleted and only show the text. </li>
     *     <li>It could have [ at the beginning but not at the end, so only will be deleted the first one.</li>
     *     <li>It could have only plain text with no [ or ] so it will be shown as is stored.</li>
     * </ul>
     * <br/><br/>
     * The main purpose of this function is to show <b> gender without [ or ]</b>.
     */
    private void fillGender()
    {
        if(media.getGender() != null && media.getGender().size() > 0)
            if(!media.getGender().get(0).equalsIgnoreCase(""))
                if(media.getGender().toString().contains("["))
                    if(media.getGender().toString().contains("]"))
                        ((TextView) findViewById(R.id.tv_media_gender)).setText(media.getGender().toString().split("\\[")[1].split("]")[0]);
                    else
                        ((TextView) findViewById(R.id.tv_media_gender)).setText(media.getGender().toString().split("\\[")[1]);
                else
                    ((TextView) findViewById(R.id.tv_media_gender)).setText(media.getGender().toString());
    }

    /**
     * Depending on the type of the media that has been passed it will fill different values.
     * <br/>Types will be:
     * <ul>
     *     <li>Book</li>
     *     <li>Music</li>
     *     <li>Serie</li>
     *     <li>Movie</li>
     * </ul>
     */
    private void fillByType()
    {
        if(media.getClass().equals(Book.class))
            fillValuesForBook();
        else if(media.getClass().equals(Music.class))
            fillValuesForMusic();
        else if(media.getClass().equals(Serie.class))
            fillValuesForSerie();
        else if(media.getClass().equals(Movie.class))
            fillValuesForMovie();
    }

    /**
     * Fill all the values of the Activity when the media is a Book so only this type contains it.
     */
    private void fillValuesForBook()
    {
        Book book = (Book) media;
        if(book.getPublisher() != null)
            ((TextView)findViewById(R.id.tv_media_publisher)).setText(book.getPublisher());
        if(book.getPlot() != null)
            ((TextView)findViewById(R.id.tv_media_plot)).setText(book.getPlot());
    }

    /**
     * Fill all the values of the Activity when the media is a song (Music) so only this type contains it.
     */
    private void fillValuesForMusic()
    {
        Music music = (Music) media;
        if(music.getDuration() != null)
            ((TextView)findViewById(R.id.tv_media_duration)).setText(music.getDuration());
        if(music.getPublisher() != null)
            ((TextView)findViewById(R.id.tv_media_publisher)).setText(music.getPublisher());
    }

    /**
     * Fill all the values of the Activity when the media is a Serie so only this type contains it.
     */
    private void fillValuesForSerie()
    {
        Serie serie = (Serie) media;
        if(serie.getPlot() != null)
            ((TextView)findViewById(R.id.tv_media_plot)).setText(serie.getPlot());
    }

    /**
     * Fill all the values of the Activity when the media is a Movie so only this type contains it.
     */
    private void fillValuesForMovie()
    {
        Movie movie = (Movie) media;
        if(movie.getPlot() != null)
            ((TextView)findViewById(R.id.tv_media_plot)).setText(movie.getPlot());
        if(movie.getDirector() != null)
            ((TextView)findViewById(R.id.tv_media_publisher)).setText(movie.getDirector());
    }

    /**
     * Change the theme of the current Activity to match the system configuration or the selection of the User.
     * <br/><br/>
     * For the automatic configuration, it will depend on the system version we are running the App. If it's Android 10 or newer it will use the option included by Google.
     * In the case is Android 9 (Android Pie) it will use the battery saver to decide the theme of the application.
     * @param selectedText Actual theme selected by the user. If they never change it, auto will be the default option.
     * @author Pablo Oraa Lopez
     */
    private void changeTheme(@NotNull String selectedText)
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

    /**
     * Recovers the image that can be found on the web and creates the bitmap asociated to show it on the activity.
     * @param src URL of the web that contains the image.
     * @return Bitmap image of the content.
     */
    @Nullable
    private Bitmap getBitmapFromURL(@NotNull String src)
    {
        try
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            if(src.contains("http") && !src.contains("https"))
                src = src.replace("http", "https");
            URL url = new URL(src);
            return  BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
