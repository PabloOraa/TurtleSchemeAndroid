package com.example.turtlescheme.ui.dashboard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.turtlescheme.Config;
import com.example.turtlescheme.Database;
import com.example.turtlescheme.R;

import java.util.ArrayList;

public class DashboardFragment extends Fragment
{
    ArrayList<String> listName = new ArrayList<>();
    ArrayAdapter<String> adapter;
    private SQLiteDatabase connection;
    View addView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        /*View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        return root;*/
        addView = inflater.inflate(R.layout.add_list, container, false);
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        listName.add(requireActivity().getText(R.string.music).toString());
        listName.add(requireActivity().getText(R.string.books).toString());
        listName.add(requireActivity().getText(R.string.movie).toString());
        listName.add(requireActivity().getText(R.string.serie).toString());
        createDatabaseConnection();
        int totalWidth = calcNecessaryWidth();
        adapter=new ArrayAdapter<>(requireActivity(), android.R.layout.simple_list_item_1, listName);
        ((ListView)requireView().findViewById(R.id.lv_contentList)).setAdapter(adapter);
        checkTheme();

        LayoutParams layoutParams = requireActivity().findViewById(R.id.sv_searchL).getLayoutParams();
        layoutParams.width = totalWidth;
        requireActivity().findViewById(R.id.sv_searchL).setLayoutParams(layoutParams);

        createListener();
    }

    private void createDatabaseConnection()
    {
        Database db = new Database(requireActivity(),"turtlesketch.db", null, 3);
        connection = db.getWritableDatabase();
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
        AlertDialog.Builder alert = new AlertDialog.Builder(requireActivity());
        alert.setView(addView);
        alert.setTitle(requireActivity().getText(R.string.add_list));
        alert.setMessage(requireActivity().getText(R.string.add_list_message));
        alert.setPositiveButton(requireActivity().getText(R.string.add_list), (dialog, which) -> adapter.add(((EditText)addView.findViewById(R.id.et_add_list)).getText().toString()));
        alert.setNegativeButton(requireActivity().getText(R.string.cancel), (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = alert.create();
        dialog.show();
    }
}