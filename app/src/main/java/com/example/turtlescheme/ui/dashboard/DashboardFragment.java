package com.example.turtlescheme.ui.dashboard;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.turtlescheme.Config;
import com.example.turtlescheme.Database;
import com.example.turtlescheme.Multimedia.Multimedia;
import com.example.turtlescheme.Multimedia.MultimediaSerializable;
import com.example.turtlescheme.R;
import com.example.turtlescheme.ui.results.ListMedia;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment
{
    ArrayList<String> listName = new ArrayList<>();
    ArrayAdapter<String> adapter;
    List<String> listNameBackup = new ArrayList<>();
    Database db;
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
        checkPrefs();
        listNameBackup.addAll(listName);
        adapter=new ArrayAdapter<>(requireActivity(), android.R.layout.simple_list_item_1, listName);
        ((ListView)requireView().findViewById(R.id.lv_contentList)).setAdapter(adapter);
        checkTheme();

        LayoutParams layoutParams = requireActivity().findViewById(R.id.sv_searchL).getLayoutParams();
        layoutParams.width = totalWidth;
        requireActivity().findViewById(R.id.sv_searchL).setLayoutParams(layoutParams);

        createListener();
    }

    private void checkPrefs()
    {
        SharedPreferences sharedpreferences = requireContext().getSharedPreferences("lists", Context.MODE_PRIVATE);
        if(sharedpreferences.contains("listsNames"))
        {
            listName.clear();
            String names = sharedpreferences.getString("listsNames", "Music");
            names = names.replaceAll("\\[", "").replaceAll("]", "");
            for (String name : names.split(","))
                listName.add(name.trim());
        }
    }

    private void createDatabaseConnection()
    {
        db = new Database(requireActivity(),"turtlesketch.db", null, 3);
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
        im.setOnClickListener(view -> startActivity(new Intent(requireActivity(), Config.class)));
        ImageView imA = requireView().findViewById(R.id.iv_addL);
        imA.setOnClickListener(view -> addNewList());
        ListView list = requireView().findViewById(R.id.lv_contentList);
        list.setOnItemClickListener((parent, view, position, id) ->
        {
            Intent intent = new Intent(requireActivity(), ListMedia.class);
            List<Multimedia> media;
            if(listName.get(position).equalsIgnoreCase((String) requireActivity().getText(R.string.books)))
                media = new ArrayList<>(db.selectList(connection,Multimedia.BOOK));
            else if(listName.get(position).equalsIgnoreCase((String) requireActivity().getText(R.string.music)))
                media = new ArrayList<>(db.selectList(connection,Multimedia.MUSIC));
            else if(listName.get(position).equalsIgnoreCase((String) requireActivity().getText(R.string.movie)))
                media = new ArrayList<>(db.selectList(connection,Multimedia.MOVIE));
            else if(listName.get(position).equalsIgnoreCase((String) requireActivity().getText(R.string.serie)))
                media = new ArrayList<>(db.selectList(connection,Multimedia.SERIE));
            else
                media = new ArrayList<>(db.selectList(connection,listName.get(position)));
            if(!media.isEmpty())
                intent.putExtra("media", new MultimediaSerializable(media));
            else
                intent.putExtra("empty", true);
            intent.putExtra("list", true);
            intent.putExtra("listTitle", listName.get(position));
            startActivity(intent);
        });
        if(requireView().findViewById(R.id.im_filterL) != null)
            requireView().findViewById(R.id.im_filterL).setOnClickListener(v -> filterSortListener("filter"));
        if(requireView().findViewById(R.id.iv_sortL) != null)
            requireView().findViewById(R.id.iv_sortL).setOnClickListener(v -> filterSortListener("sort"));
    }

    private void addNewList()
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(requireActivity());
        alert.setView(addView);
        alert.setTitle(requireActivity().getText(R.string.add_list));
        alert.setMessage(requireActivity().getText(R.string.add_list_message));
        alert.setPositiveButton(requireActivity().getText(R.string.add_list), (dialog, which) -> {
                    adapter.add(((EditText) addView.findViewById(R.id.et_add_list)).getText().toString());
                    listNameBackup.clear();
                    listNameBackup.addAll(listName);
                    SharedPreferences sharedpreferences = requireContext().getSharedPreferences("lists", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("listsNames", listName.toString());
                    editor.apply();
                });
        alert.setNegativeButton(requireActivity().getText(R.string.cancel), (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = alert.create();
        dialog.show();
    }

    private void filterSortListener(String type)
    {
        LayoutInflater inflater = (LayoutInflater) requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.filter_sort_layout, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(requireActivity());
        alert.setView(view);
        if(type.equalsIgnoreCase("filter"))
        {
            ((ConstraintLayout)view.findViewById(R.id.ln_filter_sort)).removeView(((ConstraintLayout)view.findViewById(R.id.ln_filter_sort)).getViewById(R.id.rg_sort));
            alert.setTitle(getText(R.string.add_list));
            alert.setMessage(getText(R.string.add_list_message));
            createListenerForFuncFilter(view);
        }
        else if(type.equalsIgnoreCase("sort"))
        {
            ((ConstraintLayout)view.findViewById(R.id.ln_filter_sort)).removeView(((ConstraintLayout)view.findViewById(R.id.ln_filter_sort)).getViewById(R.id.rg_filter));
            ((ConstraintLayout)view.findViewById(R.id.ln_filter_sort)).removeView(((ConstraintLayout)view.findViewById(R.id.ln_filter_sort)).getViewById(R.id.et_number_equals_less));
            ((ConstraintLayout)view.findViewById(R.id.ln_filter_sort)).removeView(((ConstraintLayout)view.findViewById(R.id.ln_filter_sort)).getViewById(R.id.et_number_equals));
            ((ConstraintLayout)view.findViewById(R.id.ln_filter_sort)).removeView(((ConstraintLayout)view.findViewById(R.id.ln_filter_sort)).getViewById(R.id.et_number_equals_more));
            ((ConstraintLayout)view.findViewById(R.id.ln_filter_sort)).removeView(((ConstraintLayout)view.findViewById(R.id.ln_filter_sort)).getViewById(R.id.sp_type_filter));
            alert.setTitle(getText(R.string.add_list));
            alert.setMessage(getText(R.string.add_list_message));
        }

        alert.setPositiveButton(getText(R.string.apply), (dialog, which) ->
        {
            if(type.equalsIgnoreCase("filter"))
                if(((RadioButton)view.findViewById(((RadioGroup) view.findViewById(R.id.rg_filter)).getCheckedRadioButtonId())).getText().toString().equalsIgnoreCase(getString(R.string.by_content_type)))
                    setNewList("type", ((Spinner)view.findViewById(R.id.sp_type_filter)).getSelectedItem().toString());
                else if(((RadioButton)view.findViewById(((RadioGroup) view.findViewById(R.id.rg_filter)).getCheckedRadioButtonId())).getText().toString().equalsIgnoreCase(getString(R.string.by_number_equal_more)))
                    setNewList("more_eq", ((EditText)view.findViewById(R.id.et_number_equals_more)).getText().toString());
        });
        alert.setNegativeButton(getText(R.string.cancel), (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = alert.create();
        dialog.show();
    }

    private void createListenerForFuncFilter(View view)
    {
        if (view.findViewById(R.id.rg_filter) != null) {
            ((RadioGroup) view.findViewById(R.id.rg_filter)).setOnCheckedChangeListener((group, checkedId) ->
            {
                if (((RadioButton) view.findViewById(checkedId)).getText().toString().equalsIgnoreCase(getString(R.string.by_content_type))) {
                    view.findViewById(R.id.sp_type_filter).setEnabled(true);
                    view.findViewById(R.id.et_number_equals_more).setEnabled(false);
                    view.findViewById(R.id.et_number_equals).setEnabled(false);
                    view.findViewById(R.id.et_number_equals_less).setEnabled(false);
                } else if (((RadioButton) view.findViewById(checkedId)).getText().toString().equalsIgnoreCase(getString(R.string.by_number_equal_more))) {
                    view.findViewById(R.id.sp_type_filter).setEnabled(false);
                    view.findViewById(R.id.et_number_equals_more).setEnabled(true);
                    view.findViewById(R.id.et_number_equals).setEnabled(false);
                    view.findViewById(R.id.et_number_equals_less).setEnabled(false);
                } else if (((RadioButton) view.findViewById(checkedId)).getText().toString().equalsIgnoreCase(getString(R.string.by_number_equal_less))) {
                    view.findViewById(R.id.sp_type_filter).setEnabled(false);
                    view.findViewById(R.id.et_number_equals_more).setEnabled(false);
                    view.findViewById(R.id.et_number_equals).setEnabled(false);
                    view.findViewById(R.id.et_number_equals_less).setEnabled(true);
                } else if (((RadioButton) view.findViewById(checkedId)).getText().toString().equalsIgnoreCase(getString(R.string.by_number_equals))) {
                    view.findViewById(R.id.sp_type_filter).setEnabled(false);
                    view.findViewById(R.id.et_number_equals_more).setEnabled(true);
                    view.findViewById(R.id.et_number_equals).setEnabled(true);
                    view.findViewById(R.id.et_number_equals_less).setEnabled(false);
                }
            });
            view.findViewById(R.id.sp_type_filter).setEnabled(false);
            view.findViewById(R.id.et_number_equals_more).setEnabled(false);
            view.findViewById(R.id.et_number_equals).setEnabled(false);
            view.findViewById(R.id.et_number_equals_less).setEnabled(false);
        }
    }

    private void setNewList(String criteria, String selectedItem)
    {
        if(criteria.equalsIgnoreCase("type"))
        {
            /*if(selectedItem.equalsIgnoreCase(getString(R.string.movie)))
                selectedItem = Multimedia.MOVIE;
            else if(selectedItem.equalsIgnoreCase(getString(R.string.music)))
                selectedItem = Multimedia.MUSIC;
            else if(selectedItem.equalsIgnoreCase(getString(R.string.books)))
                selectedItem = Multimedia.BOOK;
            else if(selectedItem.equalsIgnoreCase(getString(R.string.serie)))
                selectedItem = Multimedia.SERIE;*/

            adapter.clear();
            for(String name : listNameBackup)
                //if(media.getType().equalsIgnoreCase(selectedItem))
                    adapter.add(name );//media);
        }
    }
}