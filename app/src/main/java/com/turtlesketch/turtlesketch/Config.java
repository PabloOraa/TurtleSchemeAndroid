package com.turtlesketch.turtlesketch;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.microsoft.device.dualscreen.core.ScreenHelper;
import com.microsoft.device.dualscreen.core.manager.ScreenModeListener;
import com.microsoft.device.dualscreen.core.manager.SurfaceDuoScreenManager;

public class Config extends AppCompatActivity
{
    private SurfaceDuoScreenManager surfaceDuoScreenManager;
    public static String theme;

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
    }

    private void configListener()
    {
        if(findViewById(R.id.sp_themes) == null)
        {
            RadioGroup group = findViewById(R.id.rg_theme);
            for(int i = 0; i < group.getChildCount();i++)
                if(((RadioButton)group.getChildAt(i)).getText().toString().equalsIgnoreCase(theme))
                    ((RadioButton) group.getChildAt(i)).setChecked(true);
            group.setOnCheckedChangeListener((radioGroup, i) -> changeTheme(((RadioButton)findViewById(radioGroup.getCheckedRadioButtonId())).getText().toString()));
        }
        else
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
}
