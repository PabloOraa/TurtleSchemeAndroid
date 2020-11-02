package com.example.turtlescheme;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.turtlescheme.Multimedia.Book;
import com.example.turtlescheme.Multimedia.Multimedia;
import com.microsoft.device.dualscreen.core.manager.SurfaceDuoScreenManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
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

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
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
        if(getIntent.getSerializableExtra("media").getClass().equals(Book.class))
            media = (Book)getIntent.getSerializableExtra("media");
        ((ImageView)findViewById(R.id.im_media_cover)).setImageBitmap(getBitmapFromURL(getIntent.getStringExtra("coverString")));
        ((TextView)findViewById(R.id.tv_media_author)).setText(getIntent.getStringExtra("author"));
        ((TextView)findViewById(R.id.tv_media_title)).setText(getIntent.getStringExtra("title"));
        if(getIntent.hasExtra("publisher"))
            ((TextView)findViewById(R.id.tv_media_publisher)).setText(getIntent.getStringExtra("publisher"));
        ((TextView)findViewById(R.id.tv_media_publishDate)).setText(getIntent.getStringExtra("publishDate"));
        if(getIntent.hasExtra("language"))
        ((TextView)findViewById(R.id.tv_media_language)).setText(getIntent.getStringExtra("language"));
        if(getIntent.hasExtra("gender"))
        ((TextView)findViewById(R.id.tv_media_gender)).setText(getIntent.getStringExtra("gender"));
        if(getIntent.hasExtra("plot"))
        ((TextView)findViewById(R.id.tv_media_plot)).setText(getIntent.getStringExtra("plot"));
        if(getIntent.hasExtra("dirAlb"))
            ((TextView)findViewById(R.id.tv_media_directorAlbum)).setText(getIntent.getStringExtra("dirAlb"));
        if(getIntent.hasExtra("duration"))
            ((TextView)findViewById(R.id.tv_media_duration)).setText(getIntent.getStringExtra("duration"));
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
