package com.example.turtlescheme.ui.home;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.turtlescheme.Config;
import com.example.turtlescheme.Database;
import com.example.turtlescheme.R;

public class HomeFragment extends Fragment
{
    private SQLiteDatabase connection;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        /*View root = inflater.inflate(R.layout.fragment_home, container, false);
        return root;*/
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        createDatabaseConnection();
        createListener();
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode)
        {
            case Configuration.UI_MODE_NIGHT_NO:
                ((ImageView)requireActivity().findViewById(R.id.IV_engranajesH)).setImageResource(R.drawable.gears);
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                ((ImageView)requireActivity().findViewById(R.id.IV_engranajesH)).setImageResource(R.drawable.gears_white);
                break;
        }
    }

    private void createDatabaseConnection()
    {
        Database db = new Database(requireActivity(),"turtlesketch.db", null, 3);
        connection = db.getWritableDatabase();
    }

    private void createListener()
    {
        ImageView im = requireView().findViewById(R.id.IV_engranajesH);
        im.setOnClickListener(view ->
        {
            startActivity(new Intent(requireActivity(), Config.class));
            requireActivity().finish();
        });
    }
}