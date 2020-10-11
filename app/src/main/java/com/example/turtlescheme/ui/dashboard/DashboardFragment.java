package com.example.turtlescheme.ui.dashboard;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.turtlescheme.R;

public class DashboardFragment extends Fragment
{
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        /*View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        return root;*/
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        int extraSpace;
        DisplayMetrics display = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(display);
        int width = display.widthPixels;
        int widthConfig = requireActivity().findViewById(R.id.im_configL).getLayoutParams().width;
        int widthSort = requireActivity().findViewById(R.id.iv_sortL).getLayoutParams().width;
        int widthFilter = requireActivity().findViewById(R.id.im_filterL).getLayoutParams().width;
        int widthAdd = requireActivity().findViewById(R.id.iv_addL).getLayoutParams().width;
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE)
            extraSpace = (16*6 * (display.densityDpi / 160)) + (550 * (display.densityDpi / 160));
        else
            extraSpace = (16*7 * (display.densityDpi / 160));



        LayoutParams layoutParams = requireActivity().findViewById(R.id.sv_searchL).getLayoutParams();
        layoutParams.width = width-(widthConfig+widthAdd+widthFilter+widthSort+extraSpace);
        requireActivity().findViewById(R.id.sv_searchL).setLayoutParams(layoutParams);

    }
}