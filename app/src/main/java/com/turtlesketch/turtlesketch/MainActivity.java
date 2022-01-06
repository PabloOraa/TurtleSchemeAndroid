package com.turtlesketch.turtlesketch;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Window;

import androidx.window.java.layout.WindowInfoTrackerCallbackAdapter;
import androidx.window.layout.WindowInfoTracker;
import androidx.window.layout.WindowLayoutInfo;
import androidx.window.layout.WindowMetrics;
import androidx.window.layout.WindowMetricsCalculator;
import com.google.android.material.bottomnavigation.BottomNavigationView;
//import com.microsoft.device.dualscreen.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.core.util.Consumer;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;

/**
 * Main Activity of the application
 */
public class MainActivity extends AppCompatActivity
{
    /**
     * Manage the behaviour of the application depending on the state of the foldable device
     */
    private WindowInfoTrackerCallbackAdapter wit;
    private WindowMetrics wm;
    private WindowMetricsCalculator wmc;
    LayoutStateChangeCallback layoutStateChangeCallback = new LayoutStateChangeCallback();

    /**
     * Connection to Database which will be used to store, retrieve and delete data.
     */
    private SQLiteDatabase connection;
    /**
     * The app version code (not the version name!) that was used on the last
     * start of the app.
     */
    private final String LAST_APP_VERSION = "1.1.1";

    /**
     * {@inheritDoc}
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        Objects.requireNonNull(getSupportActionBar()).hide(); //hide the title bar

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkTheme();

        appStart();

        wit = new WindowInfoTrackerCallbackAdapter(WindowInfoTracker.Companion.getOrCreate(this));
        wmc = WindowMetricsCalculator.Companion.getOrCreate();

        Database db = new Database(this,"turtlesketch.db", null, 3);//getResources().getStringArray(R.array.sections));
        connection = db.getWritableDatabase();
    }

    /**
     * Check if it's the first time the user opens the app, if they open it for the first time in this
     * version or if they has opened more than once in this version. Depending on that result, it will
     * do different things.
     */
    private void appStart()
    {
        switch (checkAppStart())
        {
            case NORMAL:
                // We don't want to get on the user's nerves
                break;
            case FIRST_TIME_VERSION:
                System.out.println("First time on "+LAST_APP_VERSION+" :D. I can show what's new here.");
                break;
            case FIRST_TIME:
                System.out.println("First time on the app, thank you! Here there should be a tutorial or check for backup");
                break;
            default:
                break;
        }
    }

    /**
     * Check if we have a theme stored into the Shared Preferences of the App. In the case there aren't, it will
     * store automatic theme and change based on that value.
     */
    private void checkTheme()
    {
        if (Config.theme == null)
        {
            SharedPreferences sharedpreferences = getApplicationContext().getSharedPreferences("lists", Context.MODE_PRIVATE);
            if(sharedpreferences.contains("theme"))
                Config.theme = sharedpreferences.getString("theme", getString(R.string.automatic_theme));
            else
            {
                Config.theme = getString(R.string.automatic_theme);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("theme", Config.theme);
                editor.apply();
            }
        }
        changeTheme(Config.theme);
    }

    /**
     * {@inheritDoc}
     * <br/>
     * Configure the nav bar
     */
    @Override
    protected void onStart()
    {
        super.onStart();
        wit.addWindowLayoutInfoListener(this, runOnUiThreadExecutor(), layoutStateChangeCallback);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_search).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    /**
     * {@inheritDoc}
     * <br/>
     * Remove the listener for foldables
     */
    @Override
    protected void onStop()
    {
        super.onStop();
        wit.removeWindowLayoutInfoListener(layoutStateChangeCallback);
    }

    /**
     * Change the theme of the current Activity to match the system configuration or the selection of the User.
     * <br/><br/>
     * For the automatic configuration, it will depend on the system version we are running the App. If it's Android 10 or newer it will use the option included by Google.
     * In the case is Android 9 (Android Pie) it will use the battery saver to decide the theme of the application.
     * @param selectedText Actual theme selected by the user. If they never change it, auto will be the default option.
     * @author Pablo Oraa Lopez
     */
    public void changeTheme(@NotNull String selectedText)
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

    /**
     * Distinguishes different kinds of app starts:
     * <ul>
     *     <li>First start ever ({@link #FIRST_TIME})</li>
     *     <li>First start in this version ({@link #FIRST_TIME_VERSION})</li>
     *     <li>Normal app start ({@link #NORMAL})</li>
     * </ul>
     */
    public enum AppStart
    {
        FIRST_TIME, FIRST_TIME_VERSION, NORMAL
    }

    /**
     * Finds out started for the first time (ever or in the current version).<br/>
     * <br/>
     * Note: This method is <b>not idempotent</b> only the first call will
     * determine the proper result. Any subsequent calls will only return
     * {@link AppStart#NORMAL} until the app is started again. So you might want
     * to consider caching the result!
     *
     * @return the type of app start
     */
    public AppStart checkAppStart() {
        PackageInfo pInfo;
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("lists", Context.MODE_PRIVATE);
        AppStart appStart = AppStart.NORMAL;
        try
        {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            int lastVersionCode = sharedPreferences.getInt(LAST_APP_VERSION, -1);
            int currentVersionCode;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                currentVersionCode = (int)pInfo.getLongVersionCode();
            else
                currentVersionCode = pInfo.versionCode;
            appStart = checkAppStart(currentVersionCode, lastVersionCode);
            // Update version in preferences
            sharedPreferences.edit().putInt(LAST_APP_VERSION, currentVersionCode).apply();
        } catch (PackageManager.NameNotFoundException e)
        {
            Log.w("Log", "Unable to determine current app version from package manager. Definitely assuming normal app start.");
        }
        return appStart;
    }

    /**
     * Decide if it's the first time the user open the app, the first time the user open the current version of the app or if it has opened it more than once in this version.
     * <br/> To do that it will compare <b>current code </b> and <b>stored code</b> of the app.
     * @param currentVersionCode Version of the current app that is running.
     * @param lastVersionCode Last version of the app which could be stored on the device. If it's not stored it will be -1
     * @return Resolution of the state in which the app should behaviour.
     */
    public AppStart checkAppStart(int currentVersionCode, int lastVersionCode)
    {
        if (lastVersionCode == -1)
            return AppStart.FIRST_TIME;
        else if (lastVersionCode < currentVersionCode)
            return AppStart.FIRST_TIME_VERSION;
        else if (lastVersionCode > currentVersionCode)
        {
            Log.w("Log", "Current version code (" + currentVersionCode+ ") is less then the one recognized on last startup (" + lastVersionCode + "). Definitely assuming normal app start.");
            return AppStart.NORMAL;
        }
        else
            return AppStart.NORMAL;
    }



    void updateLayout(WindowLayoutInfo windowLayoutInfo)
    {
        if(Config.appSpanned && windowLayoutInfo.getDisplayFeatures().isEmpty())
        {
            Config.appSpanned = !windowLayoutInfo.getDisplayFeatures().isEmpty();

            this.recreate();
        }
        else if(!Config.appSpanned && !windowLayoutInfo.getDisplayFeatures().isEmpty())
        {
            Config.appSpanned = !windowLayoutInfo.getDisplayFeatures().isEmpty();

            this.recreate();
        }
    }

    class LayoutStateChangeCallback implements Consumer<WindowLayoutInfo> {
        @Override
        public void accept(WindowLayoutInfo windowLayoutInfo) {
            updateLayout(windowLayoutInfo);
        }
    }

    Executor runOnUiThreadExecutor()
    {
        return new MyExecutor();
    }
    class MyExecutor implements Executor
    {
        Handler handler = new Handler(Looper.getMainLooper());
        @Override
        public void execute(Runnable command) {
            handler.post(command);
        }
    }
}