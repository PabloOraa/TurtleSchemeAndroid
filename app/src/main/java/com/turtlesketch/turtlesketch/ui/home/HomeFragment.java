package com.turtlesketch.turtlesketch.ui.home;

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
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.turtlesketch.turtlesketch.Config;
import com.turtlesketch.turtlesketch.Interfaces.OmbdAPI;
import com.turtlesketch.turtlesketch.Multimedia.OmbdGA.OmbdGA;
import com.turtlesketch.turtlesketch.ui.results.Converter;
import com.turtlesketch.turtlesketch.Database;
import com.turtlesketch.turtlesketch.Interfaces.MusicAPI;
import com.turtlesketch.turtlesketch.Interfaces.GoogleAPI;
import com.turtlesketch.turtlesketch.Multimedia.BooksGA.BooksGA;
import com.turtlesketch.turtlesketch.Multimedia.Multimedia;
import com.turtlesketch.turtlesketch.Multimedia.MultimediaSerializable;
import com.turtlesketch.turtlesketch.Multimedia.MusicGA.MusicGA;
import com.turtlesketch.turtlesketch.R;
import com.turtlesketch.turtlesketch.ui.results.ListMedia;
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
        checkTheme();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        if(resultCode == 2)
            ((EditText)requireView().findViewById(R.id.editTextTextPersonName)).setText("");
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void checkTheme()
    {
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES)
            ((ImageView) requireActivity().findViewById(R.id.IV_engranajesH)).setImageResource(R.drawable.gears_white);
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
        Retrofit query = createRetrofit("https://www.theaudiodb.com");
        MusicAPI apiService = query.create(MusicAPI.class);
        Call<MusicGA> call = apiService.getArtist(textToSearch);
        call.enqueue(new Callback<MusicGA>() {
            @Override
            public void onResponse(@NotNull Call<MusicGA> call, @NotNull Response<MusicGA> response) {
                if (response.isSuccessful())
                {
                    MusicGA music = response.body();
                    if (music != null && music.getArtists() != null && music.getArtists().size() > 0)
                        createList(new ArrayList<>(Converter.convertToMusicList(music)));
                    else if(music != null && (music.getArtists() == null || music.getArtists().size() == 0))
                        noResults();
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
        Retrofit query = createRetrofit("https://www.googleapis.com");
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
                        createList(new ArrayList<>(Converter.convertToBookList(books)));
                    else if(books != null && books.getTotalItems() == 0)
                        noResults();
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
        Retrofit query = createRetrofit("https://www.omdbapi.com");
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
                    if (mediaSM != null && mediaSM.getTotalResults() != null && Integer.parseInt(mediaSM.getTotalResults()) > 0)
                        createList(new ArrayList<>(Converter.convertToSeriesList(mediaSM)));
                    else if(mediaSM != null && (mediaSM.getTotalResults() == null || Integer.parseInt(mediaSM.getTotalResults()) == 0))
                        noResults();
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
        Retrofit query = createRetrofit("https://www.omdbapi.com");
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
                    if (mediaSM != null && mediaSM.getTotalResults() != null && Integer.parseInt(mediaSM.getTotalResults()) > 0)
                        createList(new ArrayList<>(Converter.convertToMovieList(mediaSM)));
                    else if(mediaSM != null && (mediaSM.getTotalResults() == null || Integer.parseInt(mediaSM.getTotalResults()) == 0))
                        noResults();
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

    private Retrofit createRetrofit(String url)
    {
        return new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create())).client(new OkHttpClient.Builder().build()).build();
    }

    private void createList(List<Multimedia> media)
    {
        Intent intent = new Intent(requireActivity(), ListMedia.class);
        intent.putExtra("media", new MultimediaSerializable(media));
        startActivityForResult(intent,2);
    }

    private void noResults()
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