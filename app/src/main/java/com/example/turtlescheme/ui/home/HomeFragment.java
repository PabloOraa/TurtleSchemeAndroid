package com.example.turtlescheme.ui.home;

import android.app.AlertDialog;
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
import com.example.turtlescheme.Interfaces.OmbdAPI;
import com.example.turtlescheme.Multimedia.Movie;
import com.example.turtlescheme.Multimedia.OmbdGA.OmbdGA;
import com.example.turtlescheme.Multimedia.Serie;
import com.example.turtlescheme.ui.results.Converter;
import com.example.turtlescheme.Database;
import com.example.turtlescheme.Interfaces.MusicAPI;
import com.example.turtlescheme.Interfaces.GoogleAPI;
import com.example.turtlescheme.Multimedia.Book;
import com.example.turtlescheme.Multimedia.BooksGA.BooksGA;
import com.example.turtlescheme.Multimedia.Multimedia;
import com.example.turtlescheme.Multimedia.MultimediaSerializable;
import com.example.turtlescheme.Multimedia.Music;
import com.example.turtlescheme.Multimedia.MusicGA.MusicGA;
import com.example.turtlescheme.R;
import com.example.turtlescheme.ui.results.ListMedia;
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
        im.setOnClickListener(view -> startActivity(new Intent(requireActivity(), Config.class)));
        Button bt = requireView().findViewById(R.id.bt_search);
        bt.setOnClickListener(view ->
        {
            String type = ((Spinner)requireView().findViewById(R.id.sp_types)).getSelectedItem().toString();
            String textToSearch = ((EditText)requireView().findViewById(R.id.editTextTextPersonName)).getText().toString();
            if(type.equalsIgnoreCase(requireActivity().getString(R.string.books)))
                searchBooks(textToSearch);
            else if(type.equalsIgnoreCase(requireActivity().getString(R.string.music)))
                searchMusic(textToSearch);
            else if(type.equalsIgnoreCase(requireActivity().getString(R.string.serie)))
                searchSerie(textToSearch);
            else if(type.equalsIgnoreCase(requireActivity().getString(R.string.movie)))
                searchMovie(textToSearch);
        });
    }

    private void searchMusic(String textToSearch)
    {
        /*Retrofit query = new Retrofit.Builder().baseUrl("https://api.deezer.com").addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                .setLenient()
                .create())).client(new OkHttpClient.Builder().build()).build();*/
        Retrofit query = new Retrofit.Builder().baseUrl("https://www.theaudiodb.com").addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                .setLenient()
                .create())).client(new OkHttpClient.Builder().build()).build();
        MusicAPI apiService = query.create(MusicAPI.class);
        Call<MusicGA> call = apiService.getArtist(textToSearch);
        call.enqueue(new Callback<MusicGA>() {
            @Override
            public void onResponse(@NotNull Call<MusicGA> call, @NotNull Response<MusicGA> response) {
                if (response.isSuccessful())
                {
                    MusicGA music = response.body();
                    if (music != null && music.getArtists().size() > 0)
                    {
                        List<Music> musicList = Converter.convertToMusicList(music);
                        Intent intent = new Intent(requireActivity(), ListMedia.class);
                        List<Multimedia> media = new ArrayList<>(musicList);
                        intent.putExtra("media", new MultimediaSerializable(media));
                        startActivity(intent);
                        requireActivity().finish();
                    }
                    else if(music != null && music.getArtists().size() > 0)
                    {
                        AlertDialog.Builder alert = new AlertDialog.Builder(requireActivity());
                        alert.setTitle(getText(R.string.error));
                        alert.setMessage(requireActivity().getText(R.string.no_result_found));
                        alert.setPositiveButton(requireActivity().getText(R.string.yes), (dialog, which) -> dialog.dismiss());
                        alert.setNegativeButton(requireActivity().getText(R.string.no), (dialog,which) -> dialog.dismiss());
                        AlertDialog dialog = alert.create();
                        dialog.show();
                    }
                }
                else
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
                    if(books != null && books.getTotalItems() > 0)
                    {
                        List<Book> bookList = Converter.convertToBookList(books);
                        Intent intent = new Intent(requireActivity(), ListMedia.class);
                        List<Multimedia> media = new ArrayList<>(bookList);
                        intent.putExtra("media", new MultimediaSerializable(media));
                        startActivity(intent);
                        requireActivity().finish();
                    }
                    else if(books != null && books.getTotalItems() == 0)
                    {
                        AlertDialog.Builder alert = new AlertDialog.Builder(requireActivity());
                        alert.setTitle(getText(R.string.error));
                        alert.setMessage(requireActivity().getText(R.string.no_result_found));
                        alert.setPositiveButton(requireActivity().getText(R.string.yes), (dialog, which) -> dialog.dismiss());
                        alert.setNegativeButton(requireActivity().getText(R.string.no), (dialog,which) -> dialog.dismiss());
                        AlertDialog dialog = alert.create();
                        dialog.show();
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

    private void searchSerie(String textToSearch)
    {
        Retrofit query = new Retrofit.Builder().baseUrl("https://www.omdbapi.com").addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                .setLenient()
                .create())).client(new OkHttpClient.Builder().build()).build();
        OmbdAPI apiService = query.create(OmbdAPI.class);
        Call<OmbdGA> call = apiService.getMovieSerie(textToSearch, Multimedia.SERIEOMBD);
        call.enqueue(new Callback<OmbdGA>()
        {
            @Override
            public void onResponse(@NotNull Call<OmbdGA> call, @NotNull Response<OmbdGA> response)
            {
                if (response.isSuccessful())
                {
                    OmbdGA mediaSM = response.body();
                    if (mediaSM != null && Integer.parseInt(mediaSM.getTotalResults()) > 0)
                    {
                        List<Serie> serieList = Converter.convertToSeriesList(mediaSM);
                        Intent intent = new Intent(requireActivity(), ListMedia.class);
                        List<Multimedia> media = new ArrayList<>(serieList);
                        intent.putExtra("media", new MultimediaSerializable(media));
                        startActivity(intent);
                        requireActivity().finish();
                    }
                    else if(mediaSM != null && Integer.parseInt(mediaSM.getTotalResults()) == 0)
                    {
                        AlertDialog.Builder alert = new AlertDialog.Builder(requireActivity());
                        alert.setTitle(getText(R.string.error));
                        alert.setMessage(requireActivity().getText(R.string.no_result_found));
                        alert.setPositiveButton(requireActivity().getText(R.string.yes), (dialog, which) -> dialog.dismiss());
                        alert.setNegativeButton(requireActivity().getText(R.string.no), (dialog,which) -> dialog.dismiss());
                        AlertDialog dialog = alert.create();
                        dialog.show();
                    }
                }
                else
                    System.out.println(response.errorBody());
            }

            @Override
            public void onFailure(@NotNull Call<OmbdGA> call, @NotNull Throwable t)
            {
                t.printStackTrace();
            }
        });
    }

    private void searchMovie(String textToSearch)
    {
        Retrofit query = new Retrofit.Builder().baseUrl("https://www.omdbapi.com").addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                .setLenient()
                .create())).client(new OkHttpClient.Builder().build()).build();
        OmbdAPI apiService = query.create(OmbdAPI.class);
        Call<OmbdGA> call = apiService.getMovieSerie(textToSearch, Multimedia.MOVIEOMBD);
        call.enqueue(new Callback<OmbdGA>()
        {
            @Override
            public void onResponse(@NotNull Call<OmbdGA> call, @NotNull Response<OmbdGA> response)
            {
                if (response.isSuccessful())
                {
                    OmbdGA mediaSM = response.body();
                    if (mediaSM != null && Integer.parseInt(mediaSM.getTotalResults()) > 0)
                    {
                        List<Movie> movieList = Converter.convertToMovieList(mediaSM);
                        Intent intent = new Intent(requireActivity(), ListMedia.class);
                        List<Multimedia> media = new ArrayList<>(movieList);
                        intent.putExtra("media", new MultimediaSerializable(media));
                        startActivity(intent);
                        requireActivity().finish();
                    }
                    else if(mediaSM != null && Integer.parseInt(mediaSM.getTotalResults()) == 0)
                    {
                        AlertDialog.Builder alert = new AlertDialog.Builder(requireActivity());
                        alert.setTitle(getText(R.string.error));
                        alert.setMessage(requireActivity().getText(R.string.no_result_found));
                        alert.setPositiveButton(requireActivity().getText(R.string.yes), (dialog, which) -> dialog.dismiss());
                        alert.setNegativeButton(requireActivity().getText(R.string.no), (dialog,which) -> dialog.dismiss());
                        AlertDialog dialog = alert.create();
                        dialog.show();
                    }
                }
                else
                    System.out.println(response.errorBody());
            }

            @Override
            public void onFailure(@NotNull Call<OmbdGA> call, @NotNull Throwable t)
            {
                t.printStackTrace();
            }
        });
    }
}