package com.turtlesketch.turtlesketch;

import android.app.Activity;
import android.app.AlertDialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.gson.GsonBuilder;
import com.turtlesketch.turtlesketch.Interfaces.MySQLAPI;
import com.turtlesketch.turtlesketch.Multimedia.Book;
import com.turtlesketch.turtlesketch.Multimedia.Multimedia;

import org.jetbrains.annotations.NotNull;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddMedia extends Activity
{
    Multimedia media;
    String type;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_media);
        checkTheme(Config.theme);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        configTitleType();
        createListener();
    }

    private void configTitleType()
    {
        type = getIntent().getStringExtra("type");
        if(getIntent().hasExtra("title"))
            ((EditText)findViewById(R.id.tv_media_title_add)).setText(getIntent().getStringExtra("title"));
    }

    private void createListener()
    {
        findViewById(R.id.bt_add_media_add).setOnClickListener(v ->
        {
            String title = ((EditText)findViewById(R.id.tv_media_title_add)).getText().toString();
            String actorAuthor = ((EditText)findViewById(R.id.tv_media_author_add)).getText().toString();
            String publisherDir = ((EditText)findViewById(R.id.tv_media_publisher_add)).getText().toString();
            String duration = ((EditText)findViewById(R.id.tv_media_duration_add)).getText().toString();
            DatePicker datePicker = findViewById(R.id.tv_media_publishDate_add);
            String publishDate = datePicker.getYear() + "-" + datePicker.getMonth() + "-" + datePicker.getDayOfMonth();
            String plotDesc = ((EditText)findViewById(R.id.tv_media_plot_add)).getText().toString();
            String gender = ((EditText)findViewById(R.id.tv_media_gender_add)).getText().toString();
            String lang = ((EditText)findViewById(R.id.tv_media_language_add)).getText().toString();
            connectAndInsert(title,actorAuthor,publisherDir,duration,publishDate,plotDesc,gender,lang);
        });
    }

    private void connectAndInsert(String title, String actorAuthor, String publisherDir, String duration, String publishDate, String plotDesc, String gender, String lang)
    {
        Call<Integer> call = null;
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://turtlesketch.consulting").addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create())).client(new OkHttpClient.Builder().build()).build();
        MySQLAPI mySQLAPI = retrofit.create(MySQLAPI.class);
        if(type.equalsIgnoreCase(getString(R.string.movie)))
            call = mySQLAPI.insertMovie(title,publisherDir,actorAuthor,plotDesc,gender,"",lang,publishDate,duration);
        else if(type.equalsIgnoreCase(getString(R.string.books)))
            call = mySQLAPI.insertBook(title,actorAuthor,publisherDir,plotDesc,gender,"",lang,publishDate);
        else if(type.equalsIgnoreCase(getString(R.string.music)))
            call = mySQLAPI.insertMusic(title,actorAuthor,publisherDir,plotDesc,gender,"",lang,publishDate,duration,title);
        else if(type.equalsIgnoreCase(getString(R.string.serie)))
            call = mySQLAPI.insertSerie(title,publisherDir,actorAuthor,plotDesc,gender,"",lang,publishDate,duration,"","");

        if(call != null)
        {
            call.enqueue(new Callback<Integer>()
            {
                @Override
                public void onResponse(@NotNull Call<Integer> call, @NotNull Response<Integer> response)
                {
                    if(response.isSuccessful())
                    {
                        if(response.body() == 1)
                            successMessage();
                        else
                            errorMessgae();
                    }
                    else
                    {
                        System.out.println(response.errorBody());
                    }
                }

                @Override
                public void onFailure(@NotNull Call<Integer> call, @NotNull Throwable t)
                {
                    t.printStackTrace();
                }
            });
        }
    }

    private void errorMessgae()
    {
        //Insert NO
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(getText(R.string.error));
        alert.setMessage(getText(R.string.error_insert));
        AlertDialog dialog = alert.create();
        dialog.show();
    }

    private void successMessage()
    {
        insertIntoMySQL();
        //Insert OK
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(getText(R.string.confirm));
        alert.setMessage(getText(R.string.confirm_insert));
        alert.setPositiveButton("Ok", (dialog, which) -> finish());
        AlertDialog dialog = alert.create();
        dialog.show();
    }

    private void insertIntoMySQL()
    {
        Database db = new Database(this,"turtlesketch.db", null, 3);//getResources().getStringArray(R.array.sections));
        SQLiteDatabase connection = db.getWritableDatabase();
        db.insertMultimedia(connection, media);//media))
    }

    private void checkTheme(String selectedText)
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
}
