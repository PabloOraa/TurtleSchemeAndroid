package com.example.turtlescheme.ui.results;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.turtlescheme.Config;
import com.example.turtlescheme.MainActivity;
import com.example.turtlescheme.Multimedia.Book;
import com.example.turtlescheme.Multimedia.Multimedia;
import com.example.turtlescheme.Multimedia.MultimediaSerializable;
import com.example.turtlescheme.Multimedia.Music;
import com.example.turtlescheme.R;

import java.util.ArrayList;
import java.util.List;

public class ListMedia extends AppCompatActivity
{
    List<Multimedia> listMedia = new ArrayList<>();
    ArrayAdapterWithPhoto adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_list_media);
        MultimediaSerializable ser = (MultimediaSerializable) getIntent().getSerializableExtra("media");
        listMedia = ser.getMultimediaList();
        adapter= new ArrayAdapterWithPhoto(this, R.layout.list_results_layout, listMedia);
        adapter.setMedia((ArrayList<Multimedia>) listMedia);
        ((ListView)findViewById(R.id.lv_contentListQuery)).setAdapter(adapter);
        changeTheme(Config.theme);
    }

    @Override
    public void onStart()
    {
        super.onStart();

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

    private void createListener()
    {
        ((ListView)findViewById(R.id.lv_contentListQuery)).setOnItemClickListener((parent, view, position, id) ->
        {
            if(listMedia.get(position).getType().equalsIgnoreCase(Multimedia.BOOK))
                openIntentBook((Book)listMedia.get(position));
            else if(listMedia.get(position).getType().equalsIgnoreCase(Multimedia.MUSIC))
                openIntentMusic((Music)listMedia.get(position));
        });
    }

    private void openIntentBook(Book book)
    {
        Intent intent = new Intent(this,ViewMedia.class);
        if(book.getTitle().contains("("))
            book.setTitle(book.getTitle().substring(0,book.getTitle().indexOf("(")-1));
        if(book.getLanguage().equalsIgnoreCase("es"))
            book.setLanguage(getText(R.string.es).toString());
        else if(book.getLanguage().equalsIgnoreCase("en"))
            book.setLanguage(getText(R.string.en).toString());
        else if(book.getLanguage().equalsIgnoreCase("fr"))
            book.setLanguage(getText(R.string.fr).toString());
        else if(book.getLanguage().equalsIgnoreCase("de"))
            book.setLanguage(getText(R.string.de).toString());
        else if(book.getLanguage().equalsIgnoreCase("be"))
            book.setLanguage(getText(R.string.be).toString());
        intent.putExtra("media", book);
        startActivityForResult(intent, 2);
    }
    private void openIntentMusic(Music music)
    {
        Intent intent = new Intent(this,ViewMedia.class);
        intent.putExtra("id",music.getId());
        intent.putExtra("coverString", music.getCover());
        intent.putExtra("author", music.getActors_authors().toString().substring(1,music.getActors_authors().toString().length()-1));
        if(music.getTitle().contains("("))
            music.setTitle(music.getTitle().substring(0,music.getTitle().indexOf("(")-1));
        intent.putExtra("title", music.getTitle());
        if(music.getPublisher() != null)
            intent.putExtra("publisher", music.getPublisher());
        intent.putExtra("publishDate",music.getPublishDate());
        intent.putExtra("language", music.getLanguage());
        if(music.getGender() != null)
            intent.putExtra("gender", music.getGender().toString().substring(1,music.getGender().toString().length()-1));
        intent.putExtra("type", music.getType());
        intent.putExtra("media", music);
        intent.putExtra("dirAlb", music.getUrl());
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 2)
        {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
