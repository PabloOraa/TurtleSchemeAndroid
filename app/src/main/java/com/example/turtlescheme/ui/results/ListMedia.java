package com.example.turtlescheme.ui.results;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.turtlescheme.Config;
import com.example.turtlescheme.MainActivity;
import com.example.turtlescheme.Multimedia.Book;
import com.example.turtlescheme.Multimedia.Movie;
import com.example.turtlescheme.Multimedia.Multimedia;
import com.example.turtlescheme.Multimedia.MultimediaSerializable;
import com.example.turtlescheme.Multimedia.Music;
import com.example.turtlescheme.Multimedia.Serie;
import com.example.turtlescheme.R;

import java.util.ArrayList;
import java.util.List;

public class ListMedia extends AppCompatActivity
{
    List<Multimedia> listMedia = new ArrayList<>();
    ArrayAdapterWithPhoto adapter;
    private boolean isList = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        isList = getIntent().hasExtra("list");

        if(getIntent().hasExtra("media"))
        {
            setContentView(R.layout.view_list_media);
            MultimediaSerializable ser = (MultimediaSerializable) getIntent().getSerializableExtra("media");
            listMedia = ser.getMultimediaList();
            adapter= new ArrayAdapterWithPhoto(this, R.layout.list_results_layout, listMedia);
            adapter.setMedia((ArrayList<Multimedia>) listMedia);
            ((ListView)findViewById(R.id.lv_contentListQuery)).setAdapter(adapter);
        }
        else if(getIntent().hasExtra("empty"))
        {
            setContentView(R.layout.view_list_media_empty);
            if(getIntent().getStringExtra("listTitle").equalsIgnoreCase(getString(R.string.music)) ||
                    getIntent().getStringExtra("listTitle").equalsIgnoreCase(getString(R.string.books)) ||
                    getIntent().getStringExtra("listTitle").equalsIgnoreCase(getString(R.string.movie)) ||
                    getIntent().getStringExtra("listTitle").equalsIgnoreCase(getString(R.string.serie)))
                findViewById(R.id.iv_addL_inside_list).setVisibility(View.INVISIBLE);
        }
        changeTheme(Config.theme);
        createListener();
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        if(isList)
        {
            int totalWidth = calcNecessaryWidth();
            ViewGroup.LayoutParams layoutParams = findViewById(R.id.sv_searchL_inside_list).getLayoutParams();
            layoutParams.width = totalWidth;
            findViewById(R.id.sv_searchL_inside_list).setLayoutParams(layoutParams);
        }
        else
        {
            if(findViewById(R.id.cl_list_inside_list) != null)
                ((LinearLayout)findViewById(R.id.cl_list_inside_list).getParent()).removeView(findViewById(R.id.cl_list_inside_list));
            //findViewById(R.id.cl_list_inside_list).setVisibility(View.INVISIBLE);
        }
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
        if(listMedia.size() > 0)
            ((ListView)findViewById(R.id.lv_contentListQuery)).setOnItemClickListener((parent, view, position, id) ->
            {
                if(listMedia.get(position).getType().equalsIgnoreCase(Multimedia.BOOK))
                    openIntentBook((Book)listMedia.get(position));
                else if(listMedia.get(position).getType().equalsIgnoreCase(Multimedia.MUSIC))
                    openIntentMusic((Music)listMedia.get(position));
                else if(listMedia.get(position).getType().equalsIgnoreCase(Multimedia.SERIE))
                    openIntentSerie((Serie)listMedia.get(position));
                else if(listMedia.get(position).getType().equalsIgnoreCase(Multimedia.MOVIE))
                    openIntentMovie((Movie)listMedia.get(position));
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
        if(music.getTitle().contains("("))
            music.setTitle(music.getTitle().substring(0,music.getTitle().indexOf("(")-1));
        intent.putExtra("media", music);
        startActivityForResult(intent, 2);
    }
    private void openIntentSerie(Serie serie)
    {
        Intent intent = new Intent(this,ViewMedia.class);
        if(serie.getTitle().contains("("))
            serie.setTitle(serie.getTitle().substring(0,serie.getTitle().indexOf("(")-1));
        intent.putExtra("media", serie);
        startActivityForResult(intent, 2);
    }
    private void openIntentMovie(Movie movie)
    {
        Intent intent = new Intent(this,ViewMedia.class);
        if(movie.getTitle().contains("("))
            movie.setTitle(movie.getTitle().substring(0,movie.getTitle().indexOf("(")-1));
        intent.putExtra("media", movie);
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

    private int calcNecessaryWidth()
    {
        int extraSpace;
        DisplayMetrics display = new DisplayMetrics();
        int width;
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
        {
            WindowMetrics windowMetrics = requireActivity().getWindowManager().getCurrentWindowMetrics();
            Insets insets = windowMetrics.getWindowInsets().getInsetsIgnoringVisibility(WindowInsets.Type.systemBars());
            width = windowMetrics.getBounds().width() - insets.left - insets.right;
        }
        else
        {*/
        getWindowManager().getDefaultDisplay().getMetrics(display);
        width = display.widthPixels;
        //}
        int widthSort = findViewById(R.id.iv_sortL_inside_list).getLayoutParams().width;
        int widthFilter = findViewById(R.id.im_filterL_inside_list).getLayoutParams().width;
        int widthAdd = 0;
        String text = getIntent().getStringExtra("listTitle");
        if(!getIntent().getStringExtra("listTitle").equalsIgnoreCase(getString(R.string.music)) &&
                !getIntent().getStringExtra("listTitle").equalsIgnoreCase(getString(R.string.books)) &&
                !getIntent().getStringExtra("listTitle").equalsIgnoreCase(getString(R.string.movie)) &&
                !getIntent().getStringExtra("listTitle").equalsIgnoreCase(getString(R.string.serie)))
            widthAdd = findViewById(R.id.iv_addL_inside_list).getLayoutParams().width;
        int orientation = getResources().getConfiguration().orientation;
        /*if (orientation == Configuration.ORIENTATION_LANDSCAPE)
            extraSpace = (16*6 * (display.densityDpi / 160)) + (550 * (display.densityDpi / 160));
        else*/
            extraSpace = (16*6 * (display.densityDpi / 160));

        if(!getIntent().getStringExtra("listTitle").equalsIgnoreCase(getString(R.string.music)) &&
                !getIntent().getStringExtra("listTitle").equalsIgnoreCase(getString(R.string.books)) &&
                !getIntent().getStringExtra("listTitle").equalsIgnoreCase(getString(R.string.movie)) &&
                !getIntent().getStringExtra("listTitle").equalsIgnoreCase(getString(R.string.serie)))
            return width-(widthAdd+widthFilter+widthSort+extraSpace);
        else
            return width-(widthFilter+widthSort+extraSpace);
    }
}
