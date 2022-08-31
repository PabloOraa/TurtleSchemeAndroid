package com.turtlesketch.turtlesketch2.ui.home;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.turtlesketch.turtlesketch2.R;
import com.turtlesketch.turtlesketch2.AddMedia;
import com.turtlesketch.turtlesketch2.Config;
import com.turtlesketch.turtlesketch2.Interfaces.MySQLAPI;
import com.turtlesketch.turtlesketch2.Interfaces.OmbdAPI;
import com.turtlesketch.turtlesketch2.Multimedia.MYSQL.MovieGA.MovieGA;
import com.turtlesketch.turtlesketch2.Multimedia.OmbdGA.OmbdGA;
import com.turtlesketch.turtlesketch2.ui.results.Converter;
import com.turtlesketch.turtlesketch2.Interfaces.MusicAPI;
import com.turtlesketch.turtlesketch2.Interfaces.GoogleAPI;
import com.turtlesketch.turtlesketch2.Multimedia.BooksGA.BooksGA;
import com.turtlesketch.turtlesketch2.Multimedia.Multimedia;
import com.turtlesketch.turtlesketch2.Multimedia.MultimediaSerializable;
import com.turtlesketch.turtlesketch2.Multimedia.MusicGA.MusicGA;
import com.turtlesketch.turtlesketch2.ui.results.ListMedia;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
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

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if(Config.appSpanned)// || !windowLayoutInfo.getDisplayFeatures().isEmpty())
            return inflater.inflate(R.layout.fragment_home_dual, container, false);
        else
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
        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            ((ImageView) requireActivity().findViewById(R.id.IV_engranajesH)).setImageResource(R.drawable.gears_white);
        }
    }

    private void createDatabaseConnection()
    {
        /*Database db = new Database(requireActivity(),"turtlesketch2.db", null, 3);
        SQLiteDatabase connection = db.getWritableDatabase();*/
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
        showMessage();
        Retrofit query = createRetrofit("https://www.theaudiodb.com");
        MusicAPI apiService = query.create(MusicAPI.class);
        Call<MusicGA> call = apiService.getArtist(textToSearch);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<MusicGA> call, @NotNull Response<MusicGA> response) {
                if (response.isSuccessful()) {
                    MusicGA music = response.body();
                    if (music != null && music.getArtists() != null && music.getArtists().size() > 0)
                        createList(new ArrayList<>(Converter.convertToMusicList(music)));
                    else if (music != null && (music.getArtists() == null || music.getArtists().size() == 0))
                        noResults();
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
        showMessage();
        Retrofit query = createRetrofit("https://www.googleapis.com");
        GoogleAPI apiService = query.create(GoogleAPI.class);
        Call<BooksGA> call = apiService.getBooks(textToSearch);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<BooksGA> call, @NotNull Response<BooksGA> response) {
                if (response.isSuccessful()) {
                    BooksGA books = response.body();
                    if (books != null && books.getTotalItems() > 0)
                        createList(new ArrayList<>(Converter.convertToBookList(books)));
                    else if (books != null && books.getTotalItems() == 0)
                        noResults();
                } else
                    System.out.println(response.errorBody());

            }

            @Override
            public void onFailure(@NotNull Call<BooksGA> call, @NotNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void searchSerie(String textToSearch)
    {
        showMessage();
        Retrofit query = createRetrofit("https://www.omdbapi.com");
        OmbdAPI apiService = query.create(OmbdAPI.class);
        Call<OmbdGA> call = apiService.getMovieSerie(textToSearch, Multimedia.SERIEOMBD);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<OmbdGA> call, @NotNull Response<OmbdGA> response) {
                if (response.isSuccessful()) {
                    OmbdGA mediaSM = response.body();
                    if (mediaSM != null && mediaSM.getTotalResults() != null && Integer.parseInt(mediaSM.getTotalResults()) > 0)
                        createList(new ArrayList<>(Converter.convertToSeriesList(mediaSM)));
                    else if (mediaSM != null && (mediaSM.getTotalResults() == null || Integer.parseInt(mediaSM.getTotalResults()) == 0))
                        noResults();
                } else
                    System.out.println(response.errorBody());
            }

            @Override
            public void onFailure(@NotNull Call<OmbdGA> call, @NotNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void searchMovie(String textToSearch)
    {
        showMessage();
        Retrofit query = createRetrofit("https://www.omdbapi.com");
        OmbdAPI apiService = query.create(OmbdAPI.class);
        Call<OmbdGA> call = apiService.getMovieSerie(textToSearch, Multimedia.MOVIEOMBD);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<OmbdGA> call, @NotNull Response<OmbdGA> response) {
                if (response.isSuccessful()) {
                    OmbdGA mediaSM = response.body();
                    if (mediaSM != null && mediaSM.getTotalResults() != null && Integer.parseInt(mediaSM.getTotalResults()) > 0)
                        createList(new ArrayList<>(Converter.convertToMovieList(mediaSM)));
                    else if (mediaSM != null && (mediaSM.getTotalResults() == null || Integer.parseInt(mediaSM.getTotalResults()) == 0))
                        noResults();
                } else
                    System.out.println(response.errorBody());
            }

            @Override
            public void onFailure(@NotNull Call<OmbdGA> call, @NotNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void showMessage()
    {
        Toast.makeText(requireContext(),getString(R.string.searching),Toast.LENGTH_LONG).show();
    }


    @NotNull
    @Contract("_ -> new")
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
        if(!checkOwnDatabase())
        {
            AlertDialog.Builder alert = new AlertDialog.Builder(requireActivity());
            alert.setTitle(getText(R.string.error));
            alert.setMessage(requireActivity().getText(R.string.no_result_found));
            alert.setPositiveButton(requireActivity().getText(R.string.yes), (dialog, which) -> addCreateMedia());
            alert.setNegativeButton(requireActivity().getText(R.string.no), (dialog,which) -> dialog.dismiss());
            AlertDialog dialog = alert.create();
            dialog.show();
        }
    }

    private boolean checkOwnDatabase()
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String type = ((Spinner)requireView().findViewById(R.id.sp_types)).getSelectedItem().toString();
        String textToSearch = ((EditText)requireView().findViewById(R.id.editTextTextPersonName)).getText().toString();
        Retrofit retrofit = createRetrofit("https://turtlesketch.consulting");
        MySQLAPI mySQLAPI = retrofit.create(MySQLAPI.class);
        if(type.equalsIgnoreCase(getString(R.string.books)))
        {
            Call<com.turtlesketch.turtlesketch2.Multimedia.MYSQL.BooksGA.BooksGA> call = mySQLAPI.getBook(textToSearch);
            try
            {
                Response<com.turtlesketch.turtlesketch2.Multimedia.MYSQL.BooksGA.BooksGA> response = call.execute();
                if(response.isSuccessful())
                {
                    com.turtlesketch.turtlesketch2.Multimedia.MYSQL.BooksGA.BooksGA book = response.body();
                    if(book != null && Integer.parseInt(book.getTotalResults()) > 0)
                    {
                        createList(new ArrayList<>(Converter.convertToBookSQLList(book)));
                        return true;
                    }
                }
                else
                    System.out.println(response.errorBody());
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        else if(type.equalsIgnoreCase(getString(R.string.movie)))
        {
            Call<MovieGA> call = mySQLAPI.getMovie(textToSearch);
            try
            {
                Response<MovieGA> response = call.execute();
                if(response.isSuccessful())
                {
                    MovieGA movie = response.body();
                    if(movie != null && Integer.parseInt(movie.getTotalResults()) > 0)
                    {
                        createList(new ArrayList<>(Converter.convertToMovieSQLList(movie)));
                        return true;
                    }
                }
                else
                    System.out.println(response.errorBody());

            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        else if(type.equalsIgnoreCase(getString(R.string.music)))
        {
            Call<com.turtlesketch.turtlesketch2.Multimedia.MYSQL.MusicGA.MusicGA> call = mySQLAPI.getMusic(textToSearch);
            try
            {
                Response<com.turtlesketch.turtlesketch2.Multimedia.MYSQL.MusicGA.MusicGA> response = call.execute();
                if(response.isSuccessful())
                {
                    com.turtlesketch.turtlesketch2.Multimedia.MYSQL.MusicGA.MusicGA music = response.body();
                    if(music != null && Integer.parseInt(music.getTotalResults()) > 0)
                    {
                        createList(new ArrayList<>(Converter.convertToMusicSQLList(music)));
                        return true;
                    }
                }
                else
                    System.out.println(response.errorBody());

            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        else if(type.equalsIgnoreCase(getString(R.string.serie)))
        {
            Call<com.turtlesketch.turtlesketch2.Multimedia.MYSQL.SerieGA.SerieGA> call = mySQLAPI.getSerie(textToSearch);
            try
            {
                Response<com.turtlesketch.turtlesketch2.Multimedia.MYSQL.SerieGA.SerieGA> response = call.execute();
                if(response.isSuccessful())
                {
                    com.turtlesketch.turtlesketch2.Multimedia.MYSQL.SerieGA.SerieGA serie = response.body();
                    if(serie != null && Integer.parseInt(serie.getTotalResults()) > 0)
                    {
                        createList(new ArrayList<>(Converter.convertToSerieSQLList(serie)));
                        return true;
                    }
                }
                else
                    System.out.println(response.errorBody());

            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return false;
    }

    private void addCreateMedia()
    {
        Intent intent = new Intent(requireActivity(), AddMedia.class);
        intent.putExtra("type",((Spinner)requireView().findViewById(R.id.sp_types)).getSelectedItem().toString());
        if(((EditText)requireView().findViewById(R.id.editTextTextPersonName)).getText() != null)
            intent.putExtra("title",((EditText)requireView().findViewById(R.id.editTextTextPersonName)).getText().toString());
        startActivityForResult(intent, 2);
    }
}