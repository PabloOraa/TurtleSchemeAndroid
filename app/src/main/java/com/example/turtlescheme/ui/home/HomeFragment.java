package com.example.turtlescheme.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.example.turtlescheme.Config;
import com.example.turtlescheme.R;

public class HomeFragment extends Fragment
{

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
        createListener();
    }

    private void createListener()
    {
        ImageView im = requireView().findViewById(R.id.IV_engranajes);
        im.setOnClickListener(view ->
        {
            startActivity(new Intent(requireActivity(), Config.class));
            requireActivity().finish();
        });
    }
}