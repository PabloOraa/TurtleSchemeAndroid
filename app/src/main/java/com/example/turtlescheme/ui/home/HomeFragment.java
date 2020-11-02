package com.example.turtlescheme.ui.home;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.turtlescheme.Config;
import com.example.turtlescheme.Converter;
import com.example.turtlescheme.Database;
import com.example.turtlescheme.Interfaces.DeezerAPI;
import com.example.turtlescheme.Interfaces.GoogleAPI;
import com.example.turtlescheme.Multimedia.Book;
import com.example.turtlescheme.Multimedia.BooksGA.BooksGA;
import com.example.turtlescheme.Multimedia.BooksGA.Item;
import com.example.turtlescheme.Multimedia.Multimedia;
import com.example.turtlescheme.Multimedia.Music;
import com.example.turtlescheme.Multimedia.MusicGA.MusicGA;
import com.example.turtlescheme.R;
import com.example.turtlescheme.ViewMedia;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment
{
    private SQLiteDatabase connection;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        /*View root = inflater.inflate(R.layout.fragment_home, container, false);
        return root;*/
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        createDatabaseConnection();
        createListener();
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode)
        {
            case Configuration.UI_MODE_NIGHT_NO:
                ((ImageView)requireActivity().findViewById(R.id.IV_engranajesH)).setImageResource(R.drawable.gears);
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                ((ImageView)requireActivity().findViewById(R.id.IV_engranajesH)).setImageResource(R.drawable.gears_white);
                break;
        }
    }

    private void createDatabaseConnection()
    {
        Database db = new Database(requireActivity(),"turtlesketch.db", null, 3);
        connection = db.getWritableDatabase();
    }

    private void createListener()
    {
        ImageView im = requireView().findViewById(R.id.IV_engranajesH);
        im.setOnClickListener(view ->
        {
            startActivity(new Intent(requireActivity(), Config.class));
            requireActivity().finish();
        });
        Button bt = requireView().findViewById(R.id.bt_search);
        bt.setOnClickListener(view ->
        {
            String type = ((Spinner)requireView().findViewById(R.id.sp_types)).getSelectedItem().toString();
            String textToSearch = ((EditText)requireView().findViewById(R.id.editTextTextPersonName)).getText().toString();
            if(type.equalsIgnoreCase(requireActivity().getString(R.string.books)))
                searchBooks(textToSearch);
            else if(type.equalsIgnoreCase(requireActivity().getString(R.string.music)))
                searchMusic(textToSearch);
        });
    }

    private void searchMusic(String textToSearch) {
        Retrofit query = new Retrofit.Builder().baseUrl("https://api.deezer.com").addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                .setLenient()
                .create())).client(new OkHttpClient.Builder().build()).build();
        DeezerAPI apiService = query.create(DeezerAPI.class);
        Call<MusicGA> call = apiService.getMusic(textToSearch);
        call.enqueue(new Callback<MusicGA>() {
            @Override
            public void onResponse(@NotNull Call<MusicGA> call, @NotNull Response<MusicGA> response) {
                if (response.isSuccessful()) {
                    MusicGA music = response.body();
                    if (music != null) {
                        List<Music> musicList = Converter.convertToMusicList(music);
                        openIntentMusic(musicList.get(0));
                    }
                } else
                    System.out.println(response.errorBody());
            }

            @Override
            public void onFailure(@NotNull Call<MusicGA> call, @NotNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void searchBooks(String textToSearch)
    {
        Retrofit query = new Retrofit.Builder().baseUrl("https://www.googleapis.com").addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                .setLenient()
                .create())).client(new OkHttpClient.Builder().build()).build();
        GoogleAPI apiService = query.create(GoogleAPI.class);
        Call<BooksGA> call = apiService.getBooks(textToSearch);
        call.enqueue(new Callback<BooksGA>()
        {
            @Override
            public void onResponse(@NotNull Call<BooksGA> call, @NotNull Response<BooksGA> response)
            {
                if(response.isSuccessful())
                {
                    BooksGA books = response.body();
                    if(books != null)
                    {
                        List<Book> bookList = Converter.convertToBookList(books);
                        openIntentBook(bookList.get(0));
                    }
                }
                else
                    System.out.println(response.errorBody());

            }

            @Override
            public void onFailure(@NotNull Call<BooksGA> call, @NotNull Throwable t)
            {
                t.printStackTrace();
            }
        });
    }

    private void openIntentBook(Book book)
    {
        Intent intent = new Intent(requireActivity(),ViewMedia.class);
        intent.putExtra("id",book.getId());
        intent.putExtra("coverString", book.getCoverString());
        intent.putExtra("author", book.getActors_authors().toString().substring(1,book.getActors_authors().toString().length()-1));
        if(book.getTitle().contains("("))
            book.setTitle(book.getTitle().substring(0,book.getTitle().indexOf("(")-1));
        intent.putExtra("title", book.getTitle());
        intent.putExtra("publisher", book.getPublisher());
        intent.putExtra("publishDate",book.getPublishDate());
        if(book.getLanguage().equalsIgnoreCase("es"))
            book.setLanguage(requireActivity().getText(R.string.es).toString());
        else if(book.getLanguage().equalsIgnoreCase("en"))
            book.setLanguage(requireActivity().getText(R.string.en).toString());
        else if(book.getLanguage().equalsIgnoreCase("fr"))
            book.setLanguage(requireActivity().getText(R.string.fr).toString());
        else if(book.getLanguage().equalsIgnoreCase("de"))
            book.setLanguage(requireActivity().getText(R.string.de).toString());
        else if(book.getLanguage().equalsIgnoreCase("be"))
            book.setLanguage(requireActivity().getText(R.string.be).toString());
        intent.putExtra("language", book.getLanguage());
        intent.putExtra("gender", book.getGender().toString().substring(1,book.getGender().toString().length()-1));
        intent.putExtra("plot", book.getPlot());
        intent.putExtra("type", book.getType());
        intent.putExtra("media", book);
        startActivity(intent);
        requireActivity().finish();
    }
    private void openIntentMusic(Music music)
    {
        Intent intent = new Intent(requireActivity(),ViewMedia.class);
        intent.putExtra("id",music.getId());
        intent.putExtra("coverString", music.getCoverString());
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
        startActivity(intent);
        requireActivity().finish();
    }
}