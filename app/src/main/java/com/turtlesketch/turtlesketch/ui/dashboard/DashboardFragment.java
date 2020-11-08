package com.turtlesketch.turtlesketch.ui.dashboard;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Insets;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowInsets;
import android.view.WindowMetrics;
import android.widget.AdapterView;
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

import com.turtlesketch.turtlesketch.Config;
import com.turtlesketch.turtlesketch.Database;
import com.turtlesketch.turtlesketch.Multimedia.Multimedia;
import com.turtlesketch.turtlesketch.Multimedia.MultimediaSerializable;
import com.turtlesketch.turtlesketch.R;
import com.turtlesketch.turtlesketch.ui.results.ListMedia;

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
    private String optionFilter;
    private String optionSort;

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
        createDatabaseConnection();
        int totalWidth = calcNecessaryWidth();
        checkPrefs();
        if(listNameBackup.size() == 0)
            listNameBackup.addAll(listName);
        adapter=new ArrayAdapter<>(requireActivity(), android.R.layout.simple_list_item_1, listName);
        ((ListView)requireView().findViewById(R.id.lv_contentList)).setAdapter(adapter);
        checkTheme();

        LayoutParams layoutParams = requireActivity().findViewById(R.id.sv_searchL).getLayoutParams();
        layoutParams.width = totalWidth;
        if(requireActivity().isInMultiWindowMode() && getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            ViewGroup.MarginLayoutParams layoutParams2 = (ViewGroup.MarginLayoutParams) layoutParams;
            layoutParams2.leftMargin = (16 * (getResources().getDisplayMetrics().densityDpi / 160));
            layoutParams2.width = totalWidth;
            requireActivity().findViewById(R.id.sv_searchL).setLayoutParams(layoutParams2);
        }
        else
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
        else
        {
            listName.add(requireActivity().getText(R.string.music).toString());
            listName.add(requireActivity().getText(R.string.books).toString());
            listName.add(requireActivity().getText(R.string.movie).toString());
            listName.add(requireActivity().getText(R.string.serie).toString());
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("listsNames", listName.toString());
            editor.apply();
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
        DisplayMetrics display = getResources().getDisplayMetrics();
        int width;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
        {
            WindowMetrics windowMetrics = requireActivity().getWindowManager().getCurrentWindowMetrics();
            Insets insets = windowMetrics.getWindowInsets().getInsetsIgnoringVisibility(WindowInsets.Type.systemBars());
            width = windowMetrics.getBounds().width() - insets.left - insets.right;
        }
        else
        {
            requireActivity().getApplicationContext().getDisplay().getRealMetrics(display);
            width = display.widthPixels;
        }
        int widthConfig = requireActivity().findViewById(R.id.iv_configL).getLayoutParams().width;
        int widthSort = requireActivity().findViewById(R.id.iv_sortL).getLayoutParams().width;
        int widthFilter = requireActivity().findViewById(R.id.im_filterL).getLayoutParams().width;
        int widthAdd = requireActivity().findViewById(R.id.iv_addL).getLayoutParams().width;
        int orientation = getResources().getConfiguration().orientation;
        extraSpace = (16*7 * (display.densityDpi / 160));
        if (orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            if (requireActivity().isInMultiWindowMode()) //Split Screen
            {
                extraSpace += (16 * 2 * (display.densityDpi / 160));
                width /= 2;
            }
            else
                extraSpace += (16 * 2 * (display.densityDpi / 160)) + (550 * (display.densityDpi / 160));

        }


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
        list.setLongClickable(true);
        list.setOnItemLongClickListener(this::manageLongClick);
        if(requireView().findViewById(R.id.im_filterL) != null)
            requireView().findViewById(R.id.im_filterL).setOnClickListener(v -> filterSortListener("filter"));
        if(requireView().findViewById(R.id.iv_sortL) != null)
            requireView().findViewById(R.id.iv_sortL).setOnClickListener(v -> filterSortListener("sort"));
    }

    private boolean manageLongClick(AdapterView<?> parent, View view, int position, long id)
    {
        if(!listName.get(position).equalsIgnoreCase(listNameBackup.get(0)) &&
                !listName.get(position).equalsIgnoreCase(listNameBackup.get(1)) &&
                !listName.get(position).equalsIgnoreCase(listNameBackup.get(2)) &&
                !listName.get(position).equalsIgnoreCase(listNameBackup.get(3)))
        {
            AlertDialog.Builder alert = new AlertDialog.Builder(requireActivity());
            alert.setTitle(requireActivity().getText(R.string.add_list));
            alert.setMessage(requireActivity().getText(R.string.add_list_message));
            alert.setPositiveButton(requireActivity().getText(R.string.add_list), (dialog, which) -> {
                checkDatabaseForList(listName.get(position));
                listNameBackup.remove(listName.get(position));
                adapter.remove(listName.get(position));
                SharedPreferences sharedpreferences = requireContext().getSharedPreferences("lists", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("listsNames", listNameBackup.toString());
                editor.apply();
            });
            alert.setNegativeButton(requireActivity().getText(R.string.cancel), (dialog, which) -> dialog.dismiss());
            AlertDialog dialog = alert.create();
            dialog.show();
        }
        return false;
    }

    private void checkDatabaseForList(String s)
    {
        System.out.println(s);
    }

    private void addNewList()
    {
        LayoutInflater inflater = (LayoutInflater) requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        addView = inflater.inflate(R.layout.add_list, (ViewGroup)requireView().getParent(),false);
        AlertDialog.Builder alert = new AlertDialog.Builder(requireActivity());
        alert.setView(addView);
        alert.setTitle(requireActivity().getText(R.string.add_list));
        alert.setMessage(requireActivity().getText(R.string.add_list_message));
        alert.setPositiveButton(requireActivity().getText(R.string.add_list), (dialog, which) -> {
                    adapter.add(((EditText) addView.findViewById(R.id.et_add_list)).getText().toString());
                    listNameBackup.add(((EditText) addView.findViewById(R.id.et_add_list)).getText().toString());
                    SharedPreferences sharedpreferences = requireContext().getSharedPreferences("lists", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("listsNames", listNameBackup.toString());
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
            boolean selected = false;
            if(requireView().findViewById(R.id.im_filterL) != null)
            {
               for(int i = 0; i < ((RadioGroup)view.findViewById(R.id.rg_filter)).getChildCount() && !selected;i++)
                    if(optionFilter == null || optionFilter.isEmpty() || optionFilter.equalsIgnoreCase(getString(R.string.original)))
                    {
                        ((RadioButton)((RadioGroup)view.findViewById(R.id.rg_filter)).getChildAt(i)).setChecked(true);
                        optionFilter = getString(R.string.original);
                        selected = true;
                    }
                    else
                    {
                        for(int j = 0; j < ((RadioGroup)view.findViewById(R.id.rg_filter)).getChildCount();j++)
                            if(((RadioButton)((RadioGroup)view.findViewById(R.id.rg_filter)).getChildAt(i)).getText().toString().equalsIgnoreCase(optionFilter)) {
                                ((RadioButton) ((RadioGroup) view.findViewById(R.id.rg_filter)).getChildAt(i)).setChecked(true);
                                selected = true;
                            }
                    }

            }
            ((ConstraintLayout)view.findViewById(R.id.ln_filter_sort)).removeView(((ConstraintLayout)view.findViewById(R.id.ln_filter_sort)).getViewById(R.id.rg_sort));
            alert.setTitle(getText(R.string.add_list));
            alert.setMessage(getText(R.string.add_list_message));
            createListenerForFuncFilter(view);
        }
        else if(type.equalsIgnoreCase("sort"))
        {
            boolean selected = false;
            ((ConstraintLayout)view.findViewById(R.id.ln_filter_sort)).removeView(((ConstraintLayout)view.findViewById(R.id.ln_filter_sort)).getViewById(R.id.rg_filter));
            ((ConstraintLayout)view.findViewById(R.id.ln_filter_sort)).removeView(((ConstraintLayout)view.findViewById(R.id.ln_filter_sort)).getViewById(R.id.et_number_equals_less));
            ((ConstraintLayout)view.findViewById(R.id.ln_filter_sort)).removeView(((ConstraintLayout)view.findViewById(R.id.ln_filter_sort)).getViewById(R.id.et_number_equals));
            ((ConstraintLayout)view.findViewById(R.id.ln_filter_sort)).removeView(((ConstraintLayout)view.findViewById(R.id.ln_filter_sort)).getViewById(R.id.et_number_equals_more));
            ((ConstraintLayout)view.findViewById(R.id.ln_filter_sort)).removeView(((ConstraintLayout)view.findViewById(R.id.ln_filter_sort)).getViewById(R.id.sp_type_filter));
            for(int i = 0; i < ((RadioGroup)view.findViewById(R.id.rg_sort)).getChildCount() && !selected;i++)
                if(optionSort == null || optionSort.isEmpty() || optionSort.equalsIgnoreCase(getString(R.string.original)))
                {
                    ((RadioButton)((RadioGroup)view.findViewById(R.id.rg_sort)).getChildAt(i)).setChecked(true);
                    optionSort = getString(R.string.original);
                    selected = true;
                }
                else
                {
                    for(int j = 0; j < ((RadioGroup)view.findViewById(R.id.rg_sort)).getChildCount();j++)
                        if(((RadioButton)((RadioGroup)view.findViewById(R.id.rg_sort)).getChildAt(i)).getText().toString().equalsIgnoreCase(optionSort)) {
                            ((RadioButton) ((RadioGroup) view.findViewById(R.id.rg_sort)).getChildAt(i)).setChecked(true);
                            selected = true;
                        }
                }
            alert.setTitle(getText(R.string.add_list));
            alert.setMessage(getText(R.string.add_list_message));
        }

        alert.setPositiveButton(getText(R.string.apply), (dialog, which) ->
        {
            if(type.equalsIgnoreCase("filter"))
            {
                if(((RadioButton)view.findViewById(((RadioGroup) view.findViewById(R.id.rg_filter)).getCheckedRadioButtonId())).getText().toString().equalsIgnoreCase(getString(R.string.by_content_type)))
                    setNewList(getString(R.string.by_content_type), ((Spinner)view.findViewById(R.id.sp_type_filter)).getSelectedItem().toString());
                else if(((RadioButton)view.findViewById(((RadioGroup) view.findViewById(R.id.rg_filter)).getCheckedRadioButtonId())).getText().toString().equalsIgnoreCase(getString(R.string.by_content_type)))
                    setNewList(getString(R.string.original), null);
                else if(((RadioButton)view.findViewById(((RadioGroup) view.findViewById(R.id.rg_filter)).getCheckedRadioButtonId())).getText().toString().equalsIgnoreCase(getString(R.string.by_number_equal_more)))
                    setNewList("more_eq", ((EditText)view.findViewById(R.id.et_number_equals_more)).getText().toString());
                else if(((RadioButton)view.findViewById(((RadioGroup) view.findViewById(R.id.rg_filter)).getCheckedRadioButtonId())).getText().toString().equalsIgnoreCase(getString(R.string.by_number_equal_less)))
                    setNewList("more_eq", ((EditText)view.findViewById(R.id.et_number_equals_less)).getText().toString());
                else if(((RadioButton)view.findViewById(((RadioGroup) view.findViewById(R.id.rg_filter)).getCheckedRadioButtonId())).getText().toString().equalsIgnoreCase(getString(R.string.by_number_equals)))
                    setNewList("more_eq", ((EditText)view.findViewById(R.id.et_number_equals)).getText().toString());
            }
            else if(type.equalsIgnoreCase("sort"))
            {
                if(((RadioButton)view.findViewById(((RadioGroup) view.findViewById(R.id.rg_sort)).getCheckedRadioButtonId())).getText().toString().equalsIgnoreCase(getString(R.string.alphabetically)))
                    setNewList(getString(R.string.alphabetically));
                else if(((RadioButton)view.findViewById(((RadioGroup) view.findViewById(R.id.rg_sort)).getCheckedRadioButtonId())).getText().toString().equalsIgnoreCase(getString(R.string.original)))
                    setNewList(getString(R.string.original));
                else if(((RadioButton)view.findViewById(((RadioGroup) view.findViewById(R.id.rg_sort)).getCheckedRadioButtonId())).getText().toString().equalsIgnoreCase(getString(R.string.more_less)))
                    setNewList(getString(R.string.more_less));
                else if(((RadioButton)view.findViewById(((RadioGroup) view.findViewById(R.id.rg_sort)).getCheckedRadioButtonId())).getText().toString().equalsIgnoreCase(getString(R.string.less_more)))
                    setNewList(getString(R.string.less_more));
            }

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
                if (((RadioButton) view.findViewById(checkedId)).getText().toString().equalsIgnoreCase(getString(R.string.by_content_type)))
                {
                    view.findViewById(R.id.sp_type_filter).setEnabled(true);
                    view.findViewById(R.id.et_number_equals_more).setEnabled(false);
                    view.findViewById(R.id.et_number_equals).setEnabled(false);
                    view.findViewById(R.id.et_number_equals_less).setEnabled(false);
                }
                else if (((RadioButton) view.findViewById(checkedId)).getText().toString().equalsIgnoreCase(getString(R.string.by_number_equal_more)))
                {
                    view.findViewById(R.id.sp_type_filter).setEnabled(false);
                    view.findViewById(R.id.et_number_equals_more).setEnabled(true);
                    view.findViewById(R.id.et_number_equals).setEnabled(false);
                    view.findViewById(R.id.et_number_equals_less).setEnabled(false);
                }
                else if (((RadioButton) view.findViewById(checkedId)).getText().toString().equalsIgnoreCase(getString(R.string.by_number_equal_less)))
                {
                    view.findViewById(R.id.sp_type_filter).setEnabled(false);
                    view.findViewById(R.id.et_number_equals_more).setEnabled(false);
                    view.findViewById(R.id.et_number_equals).setEnabled(false);
                    view.findViewById(R.id.et_number_equals_less).setEnabled(true);
                }
                else if (((RadioButton) view.findViewById(checkedId)).getText().toString().equalsIgnoreCase(getString(R.string.by_number_equals)))
                {
                    view.findViewById(R.id.sp_type_filter).setEnabled(false);
                    view.findViewById(R.id.et_number_equals_more).setEnabled(true);
                    view.findViewById(R.id.et_number_equals).setEnabled(true);
                    view.findViewById(R.id.et_number_equals_less).setEnabled(false);
                }
                else if(((RadioButton) view.findViewById(checkedId)).getText().toString().equalsIgnoreCase(getString(R.string.by_number_equals)))
                {
                    view.findViewById(R.id.sp_type_filter).setEnabled(false);
                    view.findViewById(R.id.et_number_equals_more).setEnabled(false);
                    view.findViewById(R.id.et_number_equals).setEnabled(true);
                    view.findViewById(R.id.et_number_equals_less).setEnabled(false);
                }
            });
            if(!optionFilter.equalsIgnoreCase(getString(R.string.by_content_type)))
                view.findViewById(R.id.sp_type_filter).setEnabled(false);
            if(!optionFilter.equalsIgnoreCase(getString(R.string.by_number_equal_more)))
                view.findViewById(R.id.et_number_equals_more).setEnabled(false);
            if(!optionFilter.equalsIgnoreCase(getString(R.string.by_number_equals)))
                view.findViewById(R.id.et_number_equals).setEnabled(false);
            if(!optionFilter.equalsIgnoreCase(getString(R.string.by_number_equal_less)))
                view.findViewById(R.id.et_number_equals_less).setEnabled(false);
        }
    }

    private void setNewList(String criteria)
    {
        if(listName != null && listName.size() > 0)
        {
            if(criteria.equalsIgnoreCase(getString(R.string.alphabetically)))
            {
                String media;
                for(int i = 0; i < listName.size();i++)
                {
                    media = listName.get(i);
                    for(int j = i; j < listName.size();j++)
                        if(media.compareToIgnoreCase(listName.get(j)) > 0)
                        {
                            String newMedia = listName.get(j);
                            listName.set(j,media);
                            listName.set(i,newMedia);
                            media = newMedia;
                        }
                }
                List<String> newListMultimedia = new ArrayList<>(listName);
                adapter.clear();
                adapter.addAll(newListMultimedia);
            }
            else if(criteria.equalsIgnoreCase(getString(R.string.more_less)))
            {
                int count;
                String media;
                for(int i = 0; i < listName.size();i++)
                {
                    count = db.getNumberOfContentOfAList(connection, getNormalizedName(listName.get(i)));
                    media = listName.get(i);
                    for(int j = i; j < listName.size();j++)
                        if(count < db.getNumberOfContentOfAList(connection, getNormalizedName(listName.get(j))))
                        {
                            String newMedia = listName.get(j);
                            listName.set(j,media);
                            listName.set(i,newMedia);
                            media = newMedia;
                        }
                }
                List<String> newListMultimedia = new ArrayList<>(listName);
                adapter.clear();
                adapter.addAll(newListMultimedia);
            }
            else if(criteria.equalsIgnoreCase(getString(R.string.less_more)))
            {
                int count;
                String media;
                for(int i = 0; i < listName.size();i++)
                {
                    count = db.getNumberOfContentOfAList(connection, getNormalizedName(listName.get(i)));
                    media = listName.get(i);
                    for(int j = i; j < listName.size();j++)
                        if(count > db.getNumberOfContentOfAList(connection, getNormalizedName(listName.get(j))))
                        {
                            String newMedia = listName.get(j);
                            listName.set(j,media);
                            listName.set(i,newMedia);
                            media = newMedia;
                        }
                }
                List<String> newListMultimedia = new ArrayList<>(listName);
                adapter.clear();
                adapter.addAll(newListMultimedia);
            }
            else if(criteria.equalsIgnoreCase(getString(R.string.original)))
            {
                adapter.clear();
                adapter.addAll(listNameBackup);
            }
        }
        optionSort = criteria;
    }

    private String getNormalizedName(String s)
    {
        if(s.equalsIgnoreCase(getString(R.string.books)))
            return Multimedia.BOOK;
        else if(s.equalsIgnoreCase(getString(R.string.music)))
            return Multimedia.MUSIC;
        else if(s.equalsIgnoreCase(getString(R.string.movie)))
            return Multimedia.MOVIE;
        else if(s.equalsIgnoreCase(getString(R.string.serie)))
            return Multimedia.SERIE;
        else
            return s;
    }

    private void setNewList(String criteria, String selectedItem)
    {
        if(listName != null && listName.size() > 0)
        {
            if (criteria.equalsIgnoreCase(getString(R.string.by_content_type)))
            {
                adapter.clear();

                for (Multimedia media : db.selectList(connection,getNormalizedName(selectedItem)))//listNameBackup)
                    if (media.getType().equalsIgnoreCase(getNormalizedName(selectedItem)))
                        adapter.add(selectedItem);
            }
            else if(criteria.equalsIgnoreCase(getString(R.string.original)))
            {
                adapter.clear();
                for (String media : listNameBackup)
                    adapter.add(media);
            }
        }
        optionFilter = criteria;
    }
}