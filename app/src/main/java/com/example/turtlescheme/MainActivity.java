package com.example.turtlescheme;

import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Window;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.microsoft.device.dualscreen.bottomnavigation.SurfaceDuoBottomNavigationView;
import com.microsoft.device.dualscreen.core.DisplayPosition;
import com.microsoft.device.dualscreen.core.ScreenHelper;
import com.microsoft.device.dualscreen.core.manager.ScreenModeListener;
import com.microsoft.device.dualscreen.core.manager.SurfaceDuoScreenManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.Objects;

public class MainActivity extends AppCompatActivity
{
    private SurfaceDuoScreenManager surfaceDuoScreenManager;
    private SQLiteDatabase connection;
    /**
     * The app version code (not the version name!) that was used on the last
     * start of the app.
     */
    private static final String LAST_APP_VERSION = "0.9.2";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        Objects.requireNonNull(getSupportActionBar()).hide(); //hide the title bar

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(Config.theme != null)
            changeTheme(Config.theme);
        else
        {
            Config.theme = getString(R.string.automatic_theme);
            changeTheme(getString(R.string.automatic_theme));
        }

        switch (checkAppStart())
        {
            case NORMAL:
                // We don't want to get on the user's nerves
                break;
            case FIRST_TIME_VERSION:
                System.out.println("First time on this version :D. I can show what's new here.");
                break;
            case FIRST_TIME:
                System.out.println("First time on the app, thank you! Here there should be a tutorial or check for backup");
                break;
            default:
                break;
        }

        if(ScreenHelper.isDeviceSurfaceDuo(this))
            configureDualScreen();

        Database db = new Database(this,"turtlesketch.db", null, 3);//getResources().getStringArray(R.array.sections));
        connection = db.getWritableDatabase();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_search).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
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

    public void changeTheme(String selectedText)
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
     * Distinguishes different kinds of app starts: <li>
     * <ul>
     * First start ever ({@link #FIRST_TIME})
     * </ul>
     * <ul>
     * First start in this version ({@link #FIRST_TIME_VERSION})
     * </ul>
     * <ul>
     * Normal app start ({@link #NORMAL})
     * </ul>
     *
     * @author schnatterer
     *
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
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        AppStart appStart = AppStart.NORMAL;
        try
        {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            int lastVersionCode = sharedPreferences
                    .getInt(LAST_APP_VERSION, -1);
            int currentVersionCode = pInfo.versionCode;
            appStart = checkAppStart(currentVersionCode, lastVersionCode);
            // Update version in preferences
            sharedPreferences.edit().putInt(LAST_APP_VERSION, currentVersionCode).apply();
        } catch (PackageManager.NameNotFoundException e)
        {
            Log.w("Log", "Unable to determine current app version from pacakge manager. Defenisvely assuming normal app start.");
        }
        return appStart;
    }

    public AppStart checkAppStart(int currentVersionCode, int lastVersionCode)
    {
        if (lastVersionCode == -1)
            return AppStart.FIRST_TIME;
        else if (lastVersionCode < currentVersionCode)
            return AppStart.FIRST_TIME_VERSION;
        else if (lastVersionCode > currentVersionCode)
        {
            Log.w("Log", "Current version code (" + currentVersionCode
                    + ") is less then the one recognized on last startup ("
                    + lastVersionCode
                    + "). Defenisvely assuming normal app start.");
            return AppStart.NORMAL;
        }
        else
            return AppStart.NORMAL;
    }
}