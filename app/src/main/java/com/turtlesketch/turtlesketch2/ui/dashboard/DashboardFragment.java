package com.turtlesketch.turtlesketch2.ui.dashboard;

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
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.WindowMetrics;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.turtlesketch.turtlesketch2.Config;
import com.turtlesketch.turtlesketch2.Database;
import com.turtlesketch.turtlesketch2.Multimedia.Multimedia;
import com.turtlesketch.turtlesketch2.Multimedia.MultimediaSerializable;
import com.turtlesketch2.turtlesketch2.R;
import com.turtlesketch.turtlesketch2.ui.results.ListMedia;

import org.jetbrains.annotations.NotNull;

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
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        createDatabaseConnection();
        checkSetList();
        checkTheme();
        checkSearchLayout();
        createListener();
    }

    private void createDatabaseConnection()
    {
        db = new Database(requireActivity(),"turtlesketch2.db", null, 3);
        connection = db.getWritableDatabase();
    }

    private void checkSetList()
    {
        checkPrefs();
        if(listNameBackup.size() == 0)
            listNameBackup.addAll(listName);
        adapter=new ArrayAdapter<>(requireActivity(), android.R.layout.simple_list_item_1, listName);
        ((ListView)requireView().findViewById(R.id.lv_contentList)).setAdapter(adapter);
    }

    private void checkTheme()
    {
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES)
        {
            ((ImageView) requireActivity().findViewById(R.id.iv_configL)).setImageResource(R.drawable.gears_white);
            ((ImageView) requireActivity().findViewById(R.id.im_filterL)).setImageResource(R.drawable.filter_white);
            ((ImageView) requireActivity().findViewById(R.id.iv_sortL)).setImageResource(R.drawable.sort_white);
            ((ImageView) requireActivity().findViewById(R.id.iv_addL)).setImageResource(R.drawable.add_white);
        }
    }

    private void checkSearchLayout()
    {
        int totalWidth = calcNecessaryWidth();
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
            WindowManager wm = (WindowManager) requireContext().getSystemService(Context.WINDOW_SERVICE);
            Display realDisplay = wm.getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            realDisplay.getMetrics(metrics);
            width = metrics.widthPixels;
            //width = display.widthPixels;
        }
        int widthConfig = requireActivity().findViewById(R.id.iv_configL).getLayoutParams().width;
        int widthSort = requireActivity().findViewById(R.id.iv_sortL).getLayoutParams().width;
        int widthFilter = requireActivity().findViewById(R.id.im_filterL).getLayoutParams().width;
        int widthAdd = requireActivity().findViewById(R.id.iv_addL).getLayoutParams().width;
        int orientation = getResources().getConfiguration().orientation;
        extraSpace = (16*7 * (display.densityDpi / 160));
        if (orientation == Configuration.ORIENTATION_LANDSCAPE)
            if (requireActivity().isInMultiWindowMode()) //Split Screen
            {
                extraSpace += (16*2 * (display.densityDpi / 160));
                width /= 2;
            }
            else
                extraSpace += (16*2 * (display.densityDpi / 160)) + (550 * (display.densityDpi / 160));

        return width-(widthConfig+widthAdd+widthFilter+widthSort+extraSpace);
    }

    private void createListener()
    {
        createListenerForImages();
        createListenerForList();
        createListenerForSearch();
    }

    private void createListenerForImages()
    {
        ImageView im = requireView().findViewById(R.id.iv_configL);
        im.setOnClickListener(view -> startActivity(new Intent(requireActivity(), Config.class)));
        ImageView imA = requireView().findViewById(R.id.iv_addL);
        imA.setOnClickListener(view -> addNewList());
        if(requireView().findViewById(R.id.im_filterL) != null)
            requireView().findViewById(R.id.im_filterL).setOnClickListener(v -> filterSortListener("filter"));
        if(requireView().findViewById(R.id.iv_sortL) != null)
            requireView().findViewById(R.id.iv_sortL).setOnClickListener(v -> filterSortListener("sort"));
    }

    private void createListenerForList()
    {
        ListView list = requireView().findViewById(R.id.lv_contentList);
        list.setOnItemClickListener((parent, view, position, id) -> itemListener(position));
        list.setLongClickable(true);
        list.setOnItemLongClickListener(this::manageLongClick);
    }

    private void createListenerForSearch()
    {
        SearchView search = requireView().findViewById(R.id.sv_searchL);
        search.setOnQueryTextFocusChangeListener((v, hasFocus) ->
        {
            if(!hasFocus)
                searchOnList(search.getQuery().toString());
        });
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                searchOnList(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                return false;
            }
        });
    }

    private void searchOnList(@NotNull String query)
    {
        adapter.clear();
        if(query.isEmpty())
            adapter.addAll(listNameBackup);
        else
            for(String text : listNameBackup)
                if(text.contains(query))
                    adapter.add(text);
    }

    private void itemListener(int position)
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
    }

    private boolean manageLongClick(AdapterView<?> parent, View view, int position, long id)
    {
        if(!listName.get(position).equalsIgnoreCase(listNameBackup.get(0)) &&
                !listName.get(position).equalsIgnoreCase(listNameBackup.get(1)) &&
                !listName.get(position).equalsIgnoreCase(listNameBackup.get(2)) &&
                !listName.get(position).equalsIgnoreCase(listNameBackup.get(3)))
        {
            AlertDialog.Builder alert = new AlertDialog.Builder(requireActivity());
            alert.setTitle(requireActivity().getText(R.string.delete_list));
            alert.setMessage(requireActivity().getText(R.string.delete_list_message));
            alert.setPositiveButton(requireActivity().getText(R.string.apply), (dialog, which) ->
            {
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
        if(db.getNumberOfContentOfAList(connection, s) > 0)
            db.deleteList(connection, s);
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

    private void filterSortListener(@NotNull String type)
    {
        LayoutInflater inflater = (LayoutInflater) requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.filter_sort_layout, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(requireActivity());
        alert.setView(view);
        if(type.equalsIgnoreCase("filter"))
        {
            removeForFilter(view);
            if(requireView().findViewById(R.id.im_filterL) != null)
                setCheckedForSort(view,R.id.rg_filter,optionFilter);
            alert.setTitle(getText(R.string.filter_list));
            alert.setMessage(getText(R.string.filter_list_message));
            createListenerForFuncFilter(view);
        }
        else if(type.equalsIgnoreCase("sort"))
        {
            removeForSort(view);
            setCheckedForSort(view, R.id.rg_sort,optionSort);
            alert.setTitle(getText(R.string.sort_list));
            alert.setMessage(getText(R.string.sort_list_message));
        }

        alert.setPositiveButton(getText(R.string.apply), (dialog, which) ->
        {
            if(type.equalsIgnoreCase("filter"))
                filterList(view);
            else if(type.equalsIgnoreCase("sort"))
                sortList(view);
        });
        alert.setNegativeButton(getText(R.string.cancel), (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = alert.create();
        dialog.show();
    }

    private void removeForFilter(@NotNull View view)
    {
        ((ConstraintLayout)view.findViewById(R.id.ln_filter_sort)).removeView(((ConstraintLayout)view.findViewById(R.id.ln_filter_sort)).getViewById(R.id.rg_sort));
    }

    private void setCheckedForSort(@NotNull View view, int idList, String option)
    {
        boolean selected = false;
        for(int i = 0; i < ((RadioGroup)view.findViewById(idList)).getChildCount() && !selected;i++)
            if(option == null || option.isEmpty() || option.equalsIgnoreCase(getString(R.string.original)))
            {
                ((RadioButton)((RadioGroup)view.findViewById(idList)).getChildAt(i)).setChecked(true);
                if(idList == R.id.rg_sort)
                    optionSort = getString(R.string.original);
                else if(idList == R.id.rg_filter)
                    optionFilter = getString(R.string.original);
                selected = true;
            }
            else
            {
                for(int j = 0; j < ((RadioGroup)view.findViewById(idList)).getChildCount();j++)
                    if(((RadioButton)((RadioGroup)view.findViewById(idList)).getChildAt(i)).getText().toString().equalsIgnoreCase(option))
                    {
                        ((RadioButton) ((RadioGroup) view.findViewById(idList)).getChildAt(i)).setChecked(true);
                        selected = true;
                    }
            }
    }

    private void removeForSort(@NotNull View view)
    {
        ((ConstraintLayout)view.findViewById(R.id.ln_filter_sort)).removeView(((ConstraintLayout)view.findViewById(R.id.ln_filter_sort)).getViewById(R.id.rg_filter));
        ((ConstraintLayout)view.findViewById(R.id.ln_filter_sort)).removeView(((ConstraintLayout)view.findViewById(R.id.ln_filter_sort)).getViewById(R.id.et_number_equals_less));
        ((ConstraintLayout)view.findViewById(R.id.ln_filter_sort)).removeView(((ConstraintLayout)view.findViewById(R.id.ln_filter_sort)).getViewById(R.id.et_number_equals));
        ((ConstraintLayout)view.findViewById(R.id.ln_filter_sort)).removeView(((ConstraintLayout)view.findViewById(R.id.ln_filter_sort)).getViewById(R.id.et_number_equals_more));
        ((ConstraintLayout)view.findViewById(R.id.ln_filter_sort)).removeView(((ConstraintLayout)view.findViewById(R.id.ln_filter_sort)).getViewById(R.id.sp_type_filter));
    }

    private void filterList(@NotNull View view)
    {
        if(((RadioButton)view.findViewById(((RadioGroup) view.findViewById(R.id.rg_filter)).getCheckedRadioButtonId())).getText().toString().equalsIgnoreCase(getString(R.string.by_content_type)))
            setNewList(getString(R.string.by_content_type), ((Spinner)view.findViewById(R.id.sp_type_filter)).getSelectedItem().toString());
        else if(((RadioButton)view.findViewById(((RadioGroup) view.findViewById(R.id.rg_filter)).getCheckedRadioButtonId())).getText().toString().equalsIgnoreCase(getString(R.string.original)))
            setNewList(getString(R.string.original), null);
        else if(((RadioButton)view.findViewById(((RadioGroup) view.findViewById(R.id.rg_filter)).getCheckedRadioButtonId())).getText().toString().equalsIgnoreCase(getString(R.string.by_number_equal_more)))
            setNewList(getString(R.string.by_number_equal_more), ((EditText)view.findViewById(R.id.et_number_equals_more)).getText().toString());
        else if(((RadioButton)view.findViewById(((RadioGroup) view.findViewById(R.id.rg_filter)).getCheckedRadioButtonId())).getText().toString().equalsIgnoreCase(getString(R.string.by_number_equal_less)))
            setNewList(getString(R.string.by_number_equal_less), ((EditText)view.findViewById(R.id.et_number_equals_less)).getText().toString());
        else if(((RadioButton)view.findViewById(((RadioGroup) view.findViewById(R.id.rg_filter)).getCheckedRadioButtonId())).getText().toString().equalsIgnoreCase(getString(R.string.by_number_equals)))
            setNewList(getString(R.string.by_number_equals), ((EditText)view.findViewById(R.id.et_number_equals)).getText().toString());
    }

    private void sortList(@NotNull View view)
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

    private void createListenerForFuncFilter(@NotNull View view)
    {
        if (view.findViewById(R.id.rg_filter) != null)
        {
            ((RadioGroup) view.findViewById(R.id.rg_filter)).setOnCheckedChangeListener((group, checkedId) ->
            {
                view.findViewById(R.id.sp_type_filter).setEnabled(false);
                view.findViewById(R.id.et_number_equals_more).setEnabled(false);
                view.findViewById(R.id.et_number_equals).setEnabled(false);
                view.findViewById(R.id.et_number_equals_less).setEnabled(false);
                if (((RadioButton) view.findViewById(checkedId)).getText().toString().equalsIgnoreCase(getString(R.string.by_content_type)))
                    view.findViewById(R.id.sp_type_filter).setEnabled(true);
                else if (((RadioButton) view.findViewById(checkedId)).getText().toString().equalsIgnoreCase(getString(R.string.by_number_equal_more)))
                    view.findViewById(R.id.et_number_equals_more).setEnabled(true);
                else if (((RadioButton) view.findViewById(checkedId)).getText().toString().equalsIgnoreCase(getString(R.string.by_number_equal_less)))
                    view.findViewById(R.id.et_number_equals_less).setEnabled(true);
                else if(((RadioButton) view.findViewById(checkedId)).getText().toString().equalsIgnoreCase(getString(R.string.by_number_equals)))
                    view.findViewById(R.id.et_number_equals).setEnabled(true);
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

    private void setNewList(String criteria) //For Sort
    {
        if(listName != null && listName.size() > 0)
            if(criteria.equalsIgnoreCase(getString(R.string.alphabetically)))
                sortAlphabetically();
            else if(criteria.equalsIgnoreCase(getString(R.string.more_less)))
                sortByNumber(true);
            else if(criteria.equalsIgnoreCase(getString(R.string.less_more)))
                sortByNumber(false);
            else if(criteria.equalsIgnoreCase(getString(R.string.original)))
                backToOriginal();
        optionSort = criteria;
    }

    private void backToOriginal()
    {
        adapter.clear();
        adapter.addAll(listNameBackup);
    }

    private void sortAlphabetically()
    {
        for(int i = 0; i < listName.size();i++)
        {
            String media = listName.get(i);
            for(int j = i; j < listName.size();j++)
                if(media.compareToIgnoreCase(listName.get(j)) > 0)
                    media = swipeMedia(i,j,media);
        }
        List<String> newListMultimedia = new ArrayList<>(listName);
        adapter.clear();
        adapter.addAll(newListMultimedia);
    }

    private void sortByNumber(boolean b) //True if it's more-less and false if it's less-more
    {
        for(int i = 0; i < listName.size();i++)
        {
            int count = db.getNumberOfContentOfAList(connection, getNormalizedName(listName.get(i)));
            String media = listName.get(i);
            for(int j = i; j < listName.size();j++)
                if(b)
                {
                    if(count < db.getNumberOfContentOfAList(connection, getNormalizedName(listName.get(j))))
                        media = swipeMedia(i,j, media);
                }
                else
                if(count > db.getNumberOfContentOfAList(connection, getNormalizedName(listName.get(j))))
                    media = swipeMedia(i,j,media);

        }
        List<String> newListMultimedia = new ArrayList<>(listName);
        adapter.clear();
        adapter.addAll(newListMultimedia);
    }

    private String swipeMedia(int i, int j, String media)
    {
        String newMedia = listName.get(j);
        listName.set(j,media);
        listName.set(i,newMedia);
        return newMedia;
    }

    private String getNormalizedName(@NotNull String s)
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

    private void setNewList(String criteria, String selectedItem) //For filter
    {
        if(selectedItem != null)
        {
            if(listNameBackup != null && listNameBackup.size() > 0)
            {
                if (criteria.equalsIgnoreCase(getString(R.string.by_content_type)))
                    filterByType(selectedItem);
                else if (criteria.equalsIgnoreCase(getString(R.string.by_number_equals)))
                    filterByNumber(Integer.parseInt(selectedItem));
                else if (criteria.equalsIgnoreCase(getString(R.string.by_number_equal_less)))
                    filterByNumberLess(Integer.parseInt(selectedItem));
                else if (criteria.equalsIgnoreCase(getString(R.string.by_number_equal_more)))
                    filterByNumberMore(Integer.parseInt(selectedItem));
            }
        }
        else if(criteria.equalsIgnoreCase(getString(R.string.original)))
            backToOriginal();
            optionFilter = criteria;
    }

    private void filterByType(String selectedItem)
    {
        adapter.clear();

        for (Multimedia media : db.selectList(connection,getNormalizedName(selectedItem)))
            if (media.getType().equalsIgnoreCase(getNormalizedName(selectedItem)))
                adapter.add(selectedItem);
    }

    private void filterByNumber(int num)
    {
        adapter.clear();
        for(String name : listNameBackup)
            if(num == db.getNumberOfContentOfAList(connection, getNormalizedName(name)))
                adapter.add(name);
    }

    private void filterByNumberMore(int num)
    {
        adapter.clear();
        for(String name: listNameBackup)
            if(num <= db.getNumberOfContentOfAList(connection, getNormalizedName(name)))
                adapter.add(name);
    }

    private void filterByNumberLess(int num)
    {
        adapter.clear();
        for(String name : listNameBackup)
            if(num >= db.getNumberOfContentOfAList(connection, getNormalizedName(name)))
                adapter.add(name);
    }
}