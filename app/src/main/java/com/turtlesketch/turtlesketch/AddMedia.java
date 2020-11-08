package com.turtlesketch.turtlesketch;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;

public class AddMedia extends Activity
{
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

        });
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
