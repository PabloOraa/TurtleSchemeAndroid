package com.example.turtlescheme.ui.dashboard;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Insets;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.WindowMetrics;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.turtlescheme.Config;
import com.example.turtlescheme.R;

import java.util.ArrayList;

public class DashboardFragment extends Fragment
{
    ArrayList<String> listName = new ArrayList<>();
    ArrayAdapter<String> adapter;

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
        int totalWidth = calcNecessaryWidth();
        adapter=new ArrayAdapter<>(requireActivity(), android.R.layout.simple_list_item_1, listName);
        checkTheme();

        LayoutParams layoutParams = requireActivity().findViewById(R.id.sv_searchL).getLayoutParams();
        layoutParams.width = totalWidth;
        requireActivity().findViewById(R.id.sv_searchL).setLayoutParams(layoutParams);

        createListener();
    }

    private void checkTheme()
    {
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode)
        {
            case Configuration.UI_MODE_NIGHT_NO:
                ((ImageView)requireActivity().findViewById(R.id.iv_configL)).setImageResource(R.drawable.gears);
                ((ImageView)requireActivity().findViewById(R.id.im_filterL)).setImageResource(R.drawable.filter);
                ((ImageView)requireActivity().findViewById(R.id.iv_sortL)).setImageResource(R.drawable.sort);
                ((ImageView)requireActivity().findViewById(R.id.iv_addL)).setImageResource(R.drawable.add);
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                ((ImageView)requireActivity().findViewById(R.id.iv_configL)).setImageResource(R.drawable.gears_white);
                ((ImageView)requireActivity().findViewById(R.id.im_filterL)).setImageResource(R.drawable.filter_white);
                ((ImageView)requireActivity().findViewById(R.id.iv_sortL)).setImageResource(R.drawable.sort_white);
                ((ImageView)requireActivity().findViewById(R.id.iv_addL)).setImageResource(R.drawable.add_white);
                break;
        }
    }

    private int calcNecessaryWidth()
    {
        int extraSpace;
        DisplayMetrics display = new DisplayMetrics();
        int width;
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
        {
            WindowMetrics windowMetrics = requireActivity().getWindowManager().getCurrentWindowMetrics();
            Insets insets = windowMetrics.getWindowInsets().getInsetsIgnoringVisibility(WindowInsets.Type.systemBars());
            width = windowMetrics.getBounds().width() - insets.left - insets.right;
        }
        else
        {*/
            requireActivity().getWindowManager().getDefaultDisplay().getMetrics(display);
            width = display.widthPixels;
        //}
        int widthConfig = requireActivity().findViewById(R.id.iv_configL).getLayoutParams().width;
        int widthSort = requireActivity().findViewById(R.id.iv_sortL).getLayoutParams().width;
        int widthFilter = requireActivity().findViewById(R.id.im_filterL).getLayoutParams().width;
        int widthAdd = requireActivity().findViewById(R.id.iv_addL).getLayoutParams().width;
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE)
            extraSpace = (16*6 * (display.densityDpi / 160)) + (550 * (display.densityDpi / 160));
        else
            extraSpace = (16*7 * (display.densityDpi / 160));

        return width-(widthConfig+widthAdd+widthFilter+widthSort+extraSpace);
    }

    private void createListener()
    {
        ImageView im = requireView().findViewById(R.id.iv_configL);
        im.setOnClickListener(view ->
        {
            startActivity(new Intent(requireActivity(), Config.class));
            requireActivity().finish();
        });
        ImageView imA = requireView().findViewById(R.id.iv_addL);
        imA.setOnClickListener(view -> addNewList());
    }

    private void addNewList()
    {

    }
}