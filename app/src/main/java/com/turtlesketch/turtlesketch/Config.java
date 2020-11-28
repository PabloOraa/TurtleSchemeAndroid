package com.turtlesketch.turtlesketch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.gson.GsonBuilder;
import com.microsoft.device.dualscreen.core.manager.ScreenModeListener;
import com.microsoft.device.dualscreen.core.manager.SurfaceDuoScreenManager;
import com.turtlesketch.turtlesketch.Interfaces.GoogleAPI;
import com.turtlesketch.turtlesketch.Multimedia.Book;
import com.turtlesketch.turtlesketch.Multimedia.BooksGA.BooksGA;
import com.turtlesketch.turtlesketch.Multimedia.LibraryGA.Item;
import com.turtlesketch.turtlesketch.Multimedia.LibraryGA.LibraryGA;
import com.turtlesketch.turtlesketch.ui.results.Converter;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Config extends AppCompatActivity
{
    private SurfaceDuoScreenManager surfaceDuoScreenManager;
    public static String theme;
    private String scope = "oauth2:" + Scopes.EMAIL + " " + Scopes.PROFILE + " " + "https://www.googleapis.com/auth/books";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configuration);
        changeTheme(theme);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        configListener();
        ((TextView)findViewById(R.id.tv_about)).setText(getString(R.string.about,getString(R.string.author_app),getString(R.string.app_ver)));

        if(GoogleSignIn.getLastSignedInAccount(getApplicationContext()) != null && !getSharedPreferences("lists", Context.MODE_PRIVATE).getString("tokenGoogle", "").equalsIgnoreCase(""))
            ((Button)findViewById(R.id.bt_con_imp_google)).setText(R.string.import_data);
        else
            ((Button)findViewById(R.id.bt_con_imp_google)).setText(R.string.connect);
    }

    private void configListener()
    {
        if(findViewById(R.id.sp_themes) == null)
            configureRadioButton();
        else
            configureSpinner();

        configureConnection();
    }

    //For Landscape
    private void configureRadioButton()
    {
        RadioGroup group = findViewById(R.id.rg_theme);
        for(int i = 0; i < group.getChildCount();i++)
            if(((RadioButton)group.getChildAt(i)).getText().toString().equalsIgnoreCase(theme))
                ((RadioButton) group.getChildAt(i)).setChecked(true);
        group.setOnCheckedChangeListener((radioGroup, i) -> changeTheme(((RadioButton)findViewById(radioGroup.getCheckedRadioButtonId())).getText().toString()));

    }

    //For Portrait
    private void configureSpinner()
    {
        Spinner spinner = findViewById(R.id.sp_themes);
        if(theme.equalsIgnoreCase(getString(R.string.light_theme)))
            spinner.setSelection(0);
        else if(theme.equalsIgnoreCase(getString(R.string.dark_theme)))
            spinner.setSelection(1);
        else
            spinner.setSelection(2);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                changeTheme(spinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });
    }

    private void configureConnection()
    {
        googleConnection();
        spotifyConnection();
    }

    private void googleConnection()
    {
        findViewById(R.id.bt_con_imp_google).setOnClickListener(v ->
        {
            if(((Button)findViewById(R.id.bt_con_imp_google)).getText().toString().equalsIgnoreCase(getString(R.string.connect)))
            {
                if(createConnectionGoogle())
                    ((Button)findViewById(R.id.bt_con_imp_google)).setText(R.string.import_data);
            }
            else
            {
                importListsGoogle();
            }
        });
    }

    private void spotifyConnection()
    {
        findViewById(R.id.bt_con_imp_spoti).setOnClickListener(v ->
        {
            if(((Button)findViewById(R.id.bt_con_imp_spoti)).getText().toString().equalsIgnoreCase(getString(R.string.connect)))
            {
                if(createConnectionSpotify())
                    ((Button)findViewById(R.id.bt_con_imp_spoti)).setText(R.string.import_data);
            }
            else
            {
                importListsSpotify();
            }
        });
    }

    private boolean createConnectionGoogle()
    {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        // Build a GoogleSignInClient with the options specified by gso.
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        try
        {
            if (account == null)
            {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, GoogleSignInStatusCodes.SUCCESS);
                return false;
            }
            else
                if(!getSharedPreferences("lists", Context.MODE_PRIVATE).getString("tokenGoogle", "").equalsIgnoreCase(""))
                    return true;
                else
                {
                    Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                    startActivityForResult(signInIntent, GoogleSignInStatusCodes.SUCCESS);
                    return false;
                }
        }
        catch(Exception e)
        {
            return false;
        }
    }

    private void importListsGoogle()
    {
        String token = getSharedPreferences("lists", Context.MODE_PRIVATE).getString("tokenGoogle", "");
        System.out.println("Token " + token);
        if(!token.equalsIgnoreCase(""))
        {
            Retrofit retrofit = new Retrofit.Builder().baseUrl("https://www.googleapis.com").addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create())).client(new OkHttpClient.Builder().build()).build();
            GoogleAPI apiService = retrofit.create(GoogleAPI.class);
            Call<LibraryGA> call = apiService.getLibraries("Bearer " + token);
            call.enqueue(new Callback<LibraryGA>()
            {
                @Override
                public void onResponse(@NotNull Call<LibraryGA> call, @NotNull Response<LibraryGA> response)
                {
                    if(response.isSuccessful())
                    {
                        LibraryGA libraries = response.body();
                        if(libraries.getItems().size() > 0)
                        {
                            addLists(libraries.getItems());
                            addBooks(libraries.getItems(), token);
                        }
                        else
                            System.out.println("No results");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<LibraryGA> call, @NotNull Throwable t)
                {
                    t.printStackTrace();
                }
            });
        }
    }

    private void addLists(List<Item> items)
    {
        List<String> listNameBackup = checkPrefs();
        for(Item item : items)
        {
            System.out.println("Title :: " + item.getTitle());
            if(!listNameBackup.contains(item.getTitle()))
                listNameBackup.add(item.getTitle());
        }
        SharedPreferences sharedpreferences = getSharedPreferences("lists", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("listsNames", listNameBackup.toString());
        editor.apply();
    }

    private void addBooks(List<Item> items, String token)
    {
        for(Item item : items)
        {
            Retrofit retrofit = new Retrofit.Builder().baseUrl("https://www.googleapis.com").addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create())).client(new OkHttpClient.Builder().build()).build();
            GoogleAPI apiService = retrofit.create(GoogleAPI.class);
            Call<BooksGA> call = apiService.getBooksByLibrary("Bearer " + token, String.valueOf(item.getId()));
            call.enqueue(new Callback<BooksGA>()
            {
                @Override
                public void onResponse(@NotNull Call<BooksGA> call, @NotNull Response<BooksGA> response)
                {
                    if(response.isSuccessful() && response.body().getTotalItems() > 0)
                    {
                        insertBooks(new ArrayList<>(Converter.convertToBookList(response.body())), item.getTitle());
                    }
                }

                @Override
                public void onFailure(@NotNull Call<BooksGA> call, @NotNull Throwable t)
                {
                    t.printStackTrace();
                }
            });
        }
    }

    private void insertBooks(ArrayList<Book> books, String titleList)
    {
        Database db = new Database(this,"turtlesketch.db", null, 3);//getResources().getStringArray(R.array.sections));
        SQLiteDatabase connection = db.getWritableDatabase();
        for(Book book : books)
        {
            if(!db.existsMultimedia(connection, book.getTitle()))
                db.insertMultimedia(connection,book);
            if(!db.existsMultimediaIntoList(connection, book.getId(), titleList))
                db.insertIntoList(connection,book,titleList);
        }
    }

    private boolean createConnectionSpotify()
    {
        return true;
    }

    private void importListsSpotify()
    {

    }

    private void changeTheme(String selectedText)
    {
        theme = selectedText;
        SharedPreferences sharedpreferences = getApplicationContext().getSharedPreferences("lists", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("theme", theme);
        editor.apply();
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

    private void configureDualScreen()
    {
        surfaceDuoScreenManager = SurfaceDuoScreenManager.getInstance(getApplication());
        getSurfaceDuoScreenManager().addScreenModeListener(this, new ScreenModeListener()
        {

            @Override
            public void onSwitchToSingleScreen()
            {
                //((SurfaceDuoBottomNavigationView)findViewById(R.id.nav_view)).setSurfaceDuoDisplayPosition(DisplayPosition.START);
                Log.d("Surface Duo", "Single Screen");
            }

            @Override
            public void onSwitchToDualScreen()
            {
                Log.d("Surface Duo", "Dual Screen");
                //((SurfaceDuoBottomNavigationView)findViewById(R.id.nav_view)).setSurfaceDuoDisplayPosition(DisplayPosition.DUAL);
            }

        });
    }

    public SurfaceDuoScreenManager getSurfaceDuoScreenManager()
    {
        return surfaceDuoScreenManager;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == GoogleSignInStatusCodes.SUCCESS)
        {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask)
    {
        try
        {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            String token = (getSharedPreferences("lists", Context.MODE_PRIVATE).getString("tokenGoogle", ""));
            if(token.equalsIgnoreCase(""))
                getToken();
        }
        catch (ApiException e)
        {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Error", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    public void getToken()
    {
        Config config = this;
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        Observable.fromCallable(() ->
        {
            String token = null;
            try
            {
                token = GoogleAuthUtil.getToken(config, account.getAccount(), scope);
            } catch (UserRecoverableAuthException e)
            {
                Log.e("Error", e.toString());
                Intent recover = e.getIntent();
                config.startActivityForResult(recover, GoogleSignInStatusCodes.SUCCESS);
            } catch (IOException | GoogleAuthException transientEx)
            {
                Log.e("Error", transientEx.toString());
            }

            SharedPreferences.Editor editor = config.getSharedPreferences("lists", Context.MODE_PRIVATE).edit();
            editor.putString("tokenGoogle", token);
            editor.apply();
            ((Button)config.findViewById(R.id.bt_con_imp_google)).setText(R.string.import_data);
            return token;
        }).subscribeOn(Schedulers.computation()).subscribe();
    }

    private List<String> checkPrefs()
    {
        List<String> listName = new ArrayList<>();
        SharedPreferences sharedpreferences = getSharedPreferences("lists", Context.MODE_PRIVATE);
        if(sharedpreferences.contains("listsNames"))
        {
            listName.clear();
            String names = sharedpreferences.getString("listsNames", "Music");
            names = names.replaceAll("\\[", "").replaceAll("]", "");
            for (String name : names.split(","))
                listName.add(name.trim());
        }
        else
        {
            listName.add(getText(R.string.music).toString());
            listName.add(getText(R.string.books).toString());
            listName.add(getText(R.string.movie).toString());
            listName.add(getText(R.string.serie).toString());
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("listsNames", listName.toString());
            editor.apply();
        }
        return listName;
    }
}
