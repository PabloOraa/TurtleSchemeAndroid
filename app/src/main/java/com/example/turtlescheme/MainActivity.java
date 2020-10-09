package com.example.turtlescheme;

import android.app.Activity;
import android.content.Intent;
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
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //show the activity in full screen

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

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
}