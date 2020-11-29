package com.turtlesketch.turtlesketch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
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

/**
 * Config activity
 * @author Pablo Oraa Lopez
 */
public class Config extends AppCompatActivity
{
    /**
     * Manage the behaviour of the application depending on the state of the Surface Duo.
     */
    private SurfaceDuoScreenManager surfaceDuoScreenManager;
    /**
     * Theme of the application.
     */
    public static String theme;
    /**
     * Scope of the application for Google Log in.
     */
    private final String scope = "oauth2:" + Scopes.EMAIL + " " + Scopes.PROFILE + " " + "https://www.googleapis.com/auth/books";

    /**
     * {@inheritDoc}
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configuration);
        changeTheme(theme);
    }

    /**
     * {@inheritDoc}
     * Set the about info and the buttons for the correct implementation of the connect part.
     */
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

    /**
     * Configure the connection to third party services and options for the themes.
     * @author Pablo Oraa Lopez
     */
    private void configListener()
    {
        if(findViewById(R.id.sp_themes) == null)
            configureRadioButton();
        else
            configureSpinner();

        configureConnection();
    }

    /**
     * Configure the radio buttons for Landscape mode on the Theme.
     * @author Pablo Oraa Lopez
     */
    private void configureRadioButton()
    {
        RadioGroup group = findViewById(R.id.rg_theme);
        for(int i = 0; i < group.getChildCount();i++)
            if(((RadioButton)group.getChildAt(i)).getText().toString().equalsIgnoreCase(theme))
                ((RadioButton) group.getChildAt(i)).setChecked(true);
        group.setOnCheckedChangeListener((radioGroup, i) -> changeTheme(((RadioButton)findViewById(radioGroup.getCheckedRadioButtonId())).getText().toString()));

    }

    /**
     * Configure the spinner for Portrait mode on the Theme.
     * @author Pablo Oraa Lopez
     */
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

    /**
     * Configure the connection to third partiy services, as of now being Google and Spotify.
     * @author Pablo Oraa Lopez
     */
    private void configureConnection()
    {
        googleConnection();
        spotifyConnection();
    }

    /**
     * Listener for the Google button to handle the connection.
     * @author Pablo Oraa Lopez.
     */
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

    /**
     * Listener for the Spotify button to handle the connection.
     * @author Pablo Oraa Lopez.
     */
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

    /**
     * Create the connection with Google and get the token if possible.
     * @return True if the connection has been created successfully and false if not.
     * @author Pablo Oraa Lopez
     */
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

    /**
     * Import all the lists the user has logged in and has in the Google Books web.
     * @author Pablo Oraa Lopez
     */
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

    /**
     * Add the lists to the SharedPreference so that could be accessed by the user.
     * @param items Items that contains the lists names.
     * @author Pablo Oraa Lopez
     */
    private void addLists(@NotNull List<Item> items)
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

    /**
     * Add the the books the user has in their digital Google collection
     * @param items Books received by Google on the personal collection.
     * @param token Personalized token to access Google.
     * @author Pablo Oraa Lopez
     */
    private void addBooks(@NotNull List<Item> items, String token)
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

    /**
     * Insert the books into the SQLite Database and into the list is contained on the Google
     * personal library.
     * @param books Book to be inserted.
     * @param titleList Title of the list to insert the book.
     */
    private void insertBooks(@NotNull ArrayList<Book> books, String titleList)
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

    /**
     * Create the connection with Spotify and get the token if possible.
     * @return True if the connection has been created successfully and false if not.
     * @author Pablo Oraa Lopez
     */
    private boolean createConnectionSpotify()
    {
        return true;
    }

    /**
     * Import the lists from the signed in spotify Account.
     * @author Pablo Oraa Lopez
     */
    private void importListsSpotify()
    {

    }

    /**
     * Change the theme of the current Activity to match the system configuration or the selection of the User.
     * <br/><br/>
     * For the automatic configuration, it will depend on the system version we are running the App. If it's Android 10 or newer it will use the option included by Google.
     * In the case is Android 9 (Android Pie) it will use the battery saver to decide the theme of the application.
     * @param selectedText Actual theme selected by the user. If they never change it, auto will be the default option.
     * @author Pablo Oraa Lopez
     */
    private void changeTheme(@NotNull String selectedText)
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

    /**
     * Create the necessary objects to manage the interaction with the app on Surface Duo.
     * <br/>
     * Depending on the real state of the Screen it will set the navigation bar in a different way.
     * @author Pablo Oraa Lopez
     */
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

    /**
     * Retrieves the configurated Manager for the Surface Duo created on the OnCreate function.
     * @return Surface Duo Manager object to manage the behaviour on this device.
     * @author Pablo Oraa Lopez
     */
    public SurfaceDuoScreenManager getSurfaceDuoScreenManager()
    {
        return surfaceDuoScreenManager;
    }

    /**
     * Do some task once the user has sign in correctly from the Intent.
     * @param requestCode Expected code.
     * @param resultCode Actual code.
     * @param data Intent with all the data.
     */
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

    /**
     * Handle the sign in from the user to get the token once he has accepted all the permissions.
     * @param completedTask GoogleSignInAccount completed when the user accept all.
     * @author Pablo Oraa Lopez
     */
    private void handleSignInResult(@NotNull Task<GoogleSignInAccount> completedTask)
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

    /**
     * Get the token for the Google Account to get all possible data from the Google Books API
     * @author Pablo Oraa Lopez
     */
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

    /**
     * Check the list of names from the shared preferences, or creates it if it doesn't exists in the
     * past.
     * @return List of names.
     * @author Pablo Oraa Lopez
     */
    @NotNull
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
