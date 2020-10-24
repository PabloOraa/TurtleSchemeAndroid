package com.example.turtlescheme;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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
}