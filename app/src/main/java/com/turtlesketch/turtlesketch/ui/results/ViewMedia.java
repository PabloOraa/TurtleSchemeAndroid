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

import java.io.IOException;
import java.net.URL;

public class ViewMedia extends AppCompatActivity
{
    private SurfaceDuoScreenManager surfaceDuoScreenManager;
    Multimedia media;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_media);
        changeTheme(Config.theme);
    }

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

    @Override
    public void onBackPressed()
    {
        setResult(1);
        super.onBackPressed();
    }

    private void createListener()
    {
        findViewById(R.id.bt_add_media).setOnClickListener(v ->
        {
            Database db = new Database(this,"turtlesketch.db", null, 3);//getResources().getStringArray(R.array.sections));
            SQLiteDatabase connection = db.getWritableDatabase();
            if(db.insertMultimedia(connection, media))
            {
                //Insert OK
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle(getText(R.string.confirm));
                alert.setMessage(getText(R.string.confirm_insert));
                alert.setPositiveButton("Ok", (dialog, which) ->
                {
                    dialog.dismiss();
                    setResult(2);
                    finish();
                });
                AlertDialog dialog = alert.create();
                dialog.show();
            }
            else
            {
                //Insert NO
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle(getText(R.string.error));
                alert.setMessage(getText(R.string.error_insert));
                AlertDialog dialog = alert.create();
                dialog.show();
            }
        });
    }

    private void fillMedia()
    {
        Intent getIntent = getIntent();
        media = (Multimedia) getIntent.getSerializableExtra("media");
        if(media.getCover() != null)
            ((ImageView)findViewById(R.id.im_media_cover)).setImageBitmap(getBitmapFromURL(media.getCover()));
        if(media.getActors_authors() != null)
            if(!media.getActors_authors().get(0).equalsIgnoreCase(""))
                if(media.getActors_authors().toString().contains("["))
                    if(media.getActors_authors().toString().contains("]"))
                        ((TextView)findViewById(R.id.tv_media_author)).setText(media.getActors_authors().toString().split("\\[")[1].split("]")[0]);
                    else
                        ((TextView)findViewById(R.id.tv_media_author)).setText(media.getActors_authors().toString().split("\\[")[1]);
                else
                    ((TextView)findViewById(R.id.tv_media_author)).setText(media.getActors_authors().toString());
        ((TextView)findViewById(R.id.tv_media_title)).setText(media.getTitle());
        if(media.getLanguage()!=null)
            ((TextView)findViewById(R.id.tv_media_language)).setText(media.getLanguage());
        if(media.getUrl() != null)
            ((TextView)findViewById(R.id.tv_media_directorAlbum)).setText(media.getUrl());
        if(media.getGender() != null)
            if(!media.getGender().get(0).equalsIgnoreCase(""))
                if(media.getGender().toString().contains("["))
                    if(media.getGender().toString().contains("]"))
                        ((TextView) findViewById(R.id.tv_media_gender)).setText(media.getGender().toString().split("\\[")[1].split("]")[0]);
                    else
                        ((TextView) findViewById(R.id.tv_media_gender)).setText(media.getGender().toString().split("\\[")[1]);
                else
                    ((TextView) findViewById(R.id.tv_media_gender)).setText(media.getGender().toString());

        ((TextView)findViewById(R.id.tv_media_publishDate)).setText(media.getPublishDate());
        if(media.getClass().equals(Book.class))
        {
            Book book = (Book) media;
            if(book.getPublisher() != null)
                ((TextView)findViewById(R.id.tv_media_publisher)).setText(book.getPublisher());
            if(book.getPlot() != null)
                ((TextView)findViewById(R.id.tv_media_plot)).setText(book.getPlot());
        }
        else if(media.getClass().equals(Music.class))
        {
            Music music = (Music) media;
            if(music.getDuration() != null)
                ((TextView)findViewById(R.id.tv_media_duration)).setText(music.getDuration());
            if(music.getPublisher() != null)
                ((TextView)findViewById(R.id.tv_media_publisher)).setText(music.getPublisher());
        }
        else if(media.getClass().equals(Serie.class))
        {
            Serie serie = (Serie) media;
            if(serie.getPlot() != null)
                ((TextView)findViewById(R.id.tv_media_plot)).setText(serie.getPlot());
        }
        else if(media.getClass().equals(Movie.class))
        {
            Movie movie = (Movie) media;
            if(movie.getPlot() != null)
                ((TextView)findViewById(R.id.tv_media_plot)).setText(movie.getPlot());
            if(movie.getDirector() != null)
                ((TextView)findViewById(R.id.tv_media_publisher)).setText(movie.getDirector());

        }
    }

    private void changeTheme(String selectedText)
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

    private Bitmap getBitmapFromURL(String src)
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
