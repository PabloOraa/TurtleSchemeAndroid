package com.example.turtlescheme.ui.results;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.turtlescheme.Config;
import com.example.turtlescheme.Database;
import com.example.turtlescheme.Multimedia.Book;
import com.example.turtlescheme.Multimedia.Multimedia;
import com.example.turtlescheme.Multimedia.Music;
import com.example.turtlescheme.R;
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
        createListener();
    }

    private void createListener()
    {
        findViewById(R.id.bt_add_media).setOnClickListener(v ->
        {
            Database db = new Database(this,"turtlesketch.db", null, 3);//getResources().getStringArray(R.array.sections));
            SQLiteDatabase connection = db.getWritableDatabase();
            db.insertMultimedia(connection, media);
        });
    }

    private void fillMedia()
    {
        Intent getIntent = getIntent();
        media = (Multimedia) getIntent.getSerializableExtra("media");
        ((ImageView)findViewById(R.id.im_media_cover)).setImageBitmap(getBitmapFromURL(media.getCoverString()));
        ((TextView)findViewById(R.id.tv_media_author)).setText(media.getActors_authors().toString());
        ((TextView)findViewById(R.id.tv_media_title)).setText(media.getTitle());
        if(media.getLanguage()!=null)
            ((TextView)findViewById(R.id.tv_media_language)).setText(media.getLanguage());
        if(media.getUrl() != null)
            ((TextView)findViewById(R.id.tv_media_directorAlbum)).setText(media.getUrl());
        if(media.getGender() != null)
            ((TextView)findViewById(R.id.tv_media_gender)).setText(media.getGender().toString().split("\\[")[1].split("]")[0]);
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
        }
    }

    private void changeTheme(String selectedText)
    {
        Config.theme = selectedText;
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
