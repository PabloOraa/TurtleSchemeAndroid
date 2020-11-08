package com.turtlesketch.turtlesketch.ui.results;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Insets;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowMetrics;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.turtlesketch.turtlesketch.Config;
import com.turtlesketch.turtlesketch.MainActivity;
import com.turtlesketch.turtlesketch.Multimedia.Book;
import com.turtlesketch.turtlesketch.Multimedia.Movie;
import com.turtlesketch.turtlesketch.Multimedia.Multimedia;
import com.turtlesketch.turtlesketch.Multimedia.MultimediaSerializable;
import com.turtlesketch.turtlesketch.Multimedia.Music;
import com.turtlesketch.turtlesketch.Multimedia.Serie;
import com.turtlesketch.turtlesketch.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ListMedia extends AppCompatActivity
{
    List<Multimedia> listMedia = new ArrayList<>();
    List<Multimedia> listMediaBackup = new ArrayList<>();
    ArrayAdapterWithPhoto adapter;
    private boolean isList = false;
    private String optionFilter;
    private String optionSort;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        isList = getIntent().hasExtra("list");

        if(getIntent().hasExtra("media"))
        {
            setContentView(R.layout.view_list_media);
            MultimediaSerializable ser = (MultimediaSerializable) getIntent().getSerializableExtra("media");
            listMedia = ser.getMultimediaList();
            adapter= new ArrayAdapterWithPhoto(this, R.layout.list_results_layout, listMedia);
            listMediaBackup.addAll(listMedia);
            adapter.setMedia((ArrayList<Multimedia>) listMedia);
            ((ListView)findViewById(R.id.lv_contentListQuery)).setAdapter(adapter);
        }
        else if(getIntent().hasExtra("empty"))
        {
            setContentView(R.layout.view_list_media_empty);
        }

        if(isList)
            if(isBasicList())
            {
                findViewById(R.id.iv_addL_inside_list).setVisibility(View.INVISIBLE);
                findViewById(R.id.im_filterL_inside_list).setVisibility(View.INVISIBLE);
            }
        changeTheme(Config.theme);
        createListener();
    }

    private boolean isBasicList()
    {
        return getIntent().getStringExtra("listTitle").equalsIgnoreCase(getString(R.string.music)) ||
                getIntent().getStringExtra("listTitle").equalsIgnoreCase(getString(R.string.books)) ||
                getIntent().getStringExtra("listTitle").equalsIgnoreCase(getString(R.string.movie)) ||
                getIntent().getStringExtra("listTitle").equalsIgnoreCase(getString(R.string.serie));
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        if(isList)
        {
            if(getIntent().hasExtra("listTitle"))
                setTitle(getIntent().getStringExtra("listTitle"));
            int totalWidth = calcNecessaryWidth();
            ViewGroup.LayoutParams layoutParams = findViewById(R.id.sv_searchL_inside_list).getLayoutParams();
            layoutParams.width = totalWidth;
            findViewById(R.id.sv_searchL_inside_list).setLayoutParams(layoutParams);
            checkIcons();
        }
        else
        {
            if(findViewById(R.id.cl_list_inside_list) != null)
                ((LinearLayout)findViewById(R.id.cl_list_inside_list).getParent()).removeView(findViewById(R.id.cl_list_inside_list));
        }
    }

    private void checkIcons()
    {
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES)
        {
            ((ImageView) findViewById(R.id.iv_sortL_inside_list)).setImageResource(R.drawable.sort_white);
            if (isBasicList())
            {
                ((ImageView) findViewById(R.id.im_filterL_inside_list)).setImageResource(R.drawable.filter_white);
                ((ImageView) findViewById(R.id.iv_addL_inside_list)).setImageResource(R.drawable.add_white);
            }
        }
    }

    @Override
    public void onBackPressed()
    {
        if(!isList)
            setResult(2);
        super.onBackPressed();
    }

    private void changeTheme(String selectedText)
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

    private void createListener()
    {
        if(listMedia.size() > 0)
            ((ListView)findViewById(R.id.lv_contentListQuery)).setOnItemClickListener((parent, view, position, id) ->
            {
                if(listMedia.get(position).getType().equalsIgnoreCase(Multimedia.BOOK))
                    openIntentBook((Book)listMedia.get(position));
                else if(listMedia.get(position).getType().equalsIgnoreCase(Multimedia.MUSIC))
                    openIntentMusic((Music)listMedia.get(position));
                else if(listMedia.get(position).getType().equalsIgnoreCase(Multimedia.SERIE))
                    openIntentSerie((Serie)listMedia.get(position));
                else if(listMedia.get(position).getType().equalsIgnoreCase(Multimedia.MOVIE))
                    openIntentMovie((Movie)listMedia.get(position));
            });
        if(isList)
        {
            if(findViewById(R.id.iv_sortL_inside_list) != null)
                findViewById(R.id.iv_sortL_inside_list).setOnClickListener(v -> filterSortListener("sort"));
            if(findViewById(R.id.im_filterL_inside_list) != null)
                findViewById(R.id.im_filterL_inside_list).setOnClickListener(v -> filterSortListener("filter"));
            createListenerForSearch();
        }
    }

    private void createListenerForSearch()
    {
        SearchView search = findViewById(R.id.sv_searchL_inside_list);
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
            adapter.addAll(listMediaBackup);
        else
            for(Multimedia media : listMediaBackup)
                if(media.getTitle().contains(query))
                    adapter.add(media);
    }

    private void filterSortListener(String type)
    {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.filter_sort_layout, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(view);
        if(type.equalsIgnoreCase("filter"))
        {
            if(findViewById(R.id.im_filterL_inside_list) != null)
            {
                ((ConstraintLayout)view.findViewById(R.id.ln_filter_sort)).removeView(((ConstraintLayout)view.findViewById(R.id.ln_filter_sort)).getViewById(R.id.et_number_equals_less));
                ((ConstraintLayout)view.findViewById(R.id.ln_filter_sort)).removeView(((ConstraintLayout)view.findViewById(R.id.ln_filter_sort)).getViewById(R.id.et_number_equals));
                ((ConstraintLayout)view.findViewById(R.id.ln_filter_sort)).removeView(((ConstraintLayout)view.findViewById(R.id.ln_filter_sort)).getViewById(R.id.et_number_equals_more));
                for(int i = 0; i < ((RadioGroup)view.findViewById(R.id.rg_filter)).getChildCount();i++)
                    if(!((RadioButton)((RadioGroup)view.findViewById(R.id.rg_filter)).getChildAt(i)).getText().toString().equalsIgnoreCase(getString(R.string.by_content_type)) && !((RadioButton)((RadioGroup)view.findViewById(R.id.rg_filter)).getChildAt(i)).getText().toString().equalsIgnoreCase(getString(R.string.original)))
                    {
                        ((RadioGroup) view.findViewById(R.id.rg_filter)).removeView(((RadioGroup) view.findViewById(R.id.rg_filter)).getChildAt(i));
                        i--;
                    }
                    else if(((RadioButton)((RadioGroup)view.findViewById(R.id.rg_filter)).getChildAt(i)).getText().toString().equalsIgnoreCase(getString(R.string.original)))
                        if(optionFilter == null || optionFilter.isEmpty() || optionFilter.equalsIgnoreCase(getString(R.string.original)))
                        {
                            ((RadioButton)((RadioGroup)view.findViewById(R.id.rg_filter)).getChildAt(i)).setChecked(true);
                            optionFilter = getString(R.string.original);
                        }
                        else
                        {
                            for(int j = 0; j < ((RadioGroup)view.findViewById(R.id.rg_filter)).getChildCount();j++)
                                if(((RadioButton)((RadioGroup)view.findViewById(R.id.rg_filter)).getChildAt(i)).getText().toString().equalsIgnoreCase(optionFilter))
                                    ((RadioButton)((RadioGroup)view.findViewById(R.id.rg_filter)).getChildAt(i)).setChecked(true);
                        }

            }
            //((LinearLayout)view.findViewById(R.id.ln_filter_sort)).removeView(findViewById(R.id.rg_sort));
            ((ConstraintLayout)view.findViewById(R.id.ln_filter_sort)).removeView(((ConstraintLayout)view.findViewById(R.id.ln_filter_sort)).getViewById(R.id.rg_sort));
            alert.setTitle(getText(R.string.filter_list));
            alert.setMessage(getText(R.string.filter_list_message));
            createListenerForFuncFilter(view);
        }
        else if(type.equalsIgnoreCase("sort"))
        {
            //((LinearLayout)view.findViewById(R.id.ln_filter_sort)).removeView(findViewById(R.id.rg_filter));
            ((ConstraintLayout)view.findViewById(R.id.ln_filter_sort)).removeView(((ConstraintLayout)view.findViewById(R.id.ln_filter_sort)).getViewById(R.id.rg_filter));
            ((ConstraintLayout)view.findViewById(R.id.ln_filter_sort)).removeView(((ConstraintLayout)view.findViewById(R.id.ln_filter_sort)).getViewById(R.id.et_number_equals_less));
            ((ConstraintLayout)view.findViewById(R.id.ln_filter_sort)).removeView(((ConstraintLayout)view.findViewById(R.id.ln_filter_sort)).getViewById(R.id.et_number_equals));
            ((ConstraintLayout)view.findViewById(R.id.ln_filter_sort)).removeView(((ConstraintLayout)view.findViewById(R.id.ln_filter_sort)).getViewById(R.id.et_number_equals_more));
            ((ConstraintLayout)view.findViewById(R.id.ln_filter_sort)).removeView(((ConstraintLayout)view.findViewById(R.id.ln_filter_sort)).getViewById(R.id.sp_type_filter));
            for(int i = 0; i < ((RadioGroup)view.findViewById(R.id.rg_sort)).getChildCount();i++)
                if(!((RadioButton)((RadioGroup)view.findViewById(R.id.rg_sort)).getChildAt(i)).getText().toString().equalsIgnoreCase(getString(R.string.alphabetically)) && ! ((RadioButton)((RadioGroup)view.findViewById(R.id.rg_sort)).getChildAt(i)).getText().toString().equalsIgnoreCase(getString(R.string.original)))
                {
                    ((RadioGroup) view.findViewById(R.id.rg_sort)).removeView(((RadioGroup) view.findViewById(R.id.rg_sort)).getChildAt(i));
                    i--;
                }
                else if(((RadioButton)((RadioGroup)view.findViewById(R.id.rg_sort)).getChildAt(i)).getText().toString().equalsIgnoreCase(getString(R.string.original)))
                    if(optionSort == null || optionSort.isEmpty() || optionSort.equalsIgnoreCase(getString(R.string.original)))
                    {
                        ((RadioButton)((RadioGroup)view.findViewById(R.id.rg_sort)).getChildAt(i)).setChecked(true);
                        optionSort = getString(R.string.original);
                    }
                    else
                    {
                        for(int j = 0; j < ((RadioGroup)view.findViewById(R.id.rg_sort)).getChildCount();j++)
                            if(((RadioButton)((RadioGroup)view.findViewById(R.id.rg_sort)).getChildAt(i)).getText().toString().equalsIgnoreCase(optionSort))
                                ((RadioButton)((RadioGroup)view.findViewById(R.id.rg_sort)).getChildAt(i)).setChecked(true);
                    }
            alert.setTitle(getText(R.string.sort_list));
            alert.setMessage(getText(R.string.sort_list_message));
        }

        alert.setPositiveButton(getText(R.string.apply), (dialog, which) ->
        {
            if(type.equalsIgnoreCase("filter"))
            {
                if(((RadioButton)view.findViewById(((RadioGroup) view.findViewById(R.id.rg_filter)).getCheckedRadioButtonId())).getText().toString().equalsIgnoreCase(getString(R.string.by_content_type)))
                    setNewList(getString(R.string.by_content_type), ((Spinner)view.findViewById(R.id.sp_type_filter)).getSelectedItem().toString());
                else if(((RadioButton)view.findViewById(((RadioGroup) view.findViewById(R.id.rg_filter)).getCheckedRadioButtonId())).getText().toString().equalsIgnoreCase(getString(R.string.by_content_type)))
                    setNewList(getString(R.string.original), null);
            }
            else if(type.equalsIgnoreCase("sort"))
            {
                if(((RadioButton)view.findViewById(((RadioGroup) view.findViewById(R.id.rg_sort)).getCheckedRadioButtonId())).getText().toString().equalsIgnoreCase(getString(R.string.alphabetically)))
                    setNewList(getString(R.string.alphabetically));
                else if(((RadioButton)view.findViewById(((RadioGroup) view.findViewById(R.id.rg_sort)).getCheckedRadioButtonId())).getText().toString().equalsIgnoreCase(getString(R.string.original)))
                    setNewList(getString(R.string.original));
            }
        });
        alert.setNegativeButton(getText(R.string.cancel), (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = alert.create();
        dialog.show();
    }

    private void createListenerForFuncFilter(View view)
    {
        if(view.findViewById(R.id.rg_filter) != null)
        {
            ((RadioGroup)view.findViewById(R.id.rg_filter)).setOnCheckedChangeListener((group, checkedId) ->
            {
                if(((RadioButton)view.findViewById(checkedId)).getText().toString().equalsIgnoreCase(getString(R.string.by_content_type)))
                    view.findViewById(R.id.sp_type_filter).setEnabled(true);
                else if(((RadioButton)view.findViewById(checkedId)).getText().toString().equalsIgnoreCase(getString(R.string.by_content_type)))
                    view.findViewById(R.id.sp_type_filter).setEnabled(false);
            });
            if(!optionFilter.equalsIgnoreCase(getString(R.string.by_content_type)))
                view.findViewById(R.id.sp_type_filter).setEnabled(false);
        }
    }

    private void setNewList(String criteria)
    {
        if(listMedia != null && listMedia.size() > 0)
        {
            if(criteria.equalsIgnoreCase(getString(R.string.alphabetically)))
            {
                Multimedia media;
                for(int i = 0; i < listMedia.size();i++)
                {
                    media = listMedia.get(i);
                    for(int j = i; j < listMedia.size();j++)
                        if(media.getTitle().compareToIgnoreCase(listMedia.get(j).getTitle()) > 0)
                        {
                            Multimedia newMedia = listMedia.get(i);
                            listMedia.set(i,media);
                            listMedia.set(j,newMedia);
                        }
                }
                List<Multimedia> newListMultimedia = new ArrayList<>(listMedia);
                adapter.clear();
                adapter.addAll(newListMultimedia);
            }
            else if(criteria.equalsIgnoreCase(getString(R.string.original)))
            {
                adapter.clear();
                adapter.addAll(listMediaBackup);
            }
        }
        optionSort = criteria;
    }

    private void setNewList(String criteria, String selectedItem)
    {
        if(listMedia != null && listMedia.size() > 0)
        {
            if (criteria.equalsIgnoreCase(getString(R.string.by_content_type)))
            {
                if (selectedItem.equalsIgnoreCase(getString(R.string.movie)))
                    selectedItem = Multimedia.MOVIE;
                else if (selectedItem.equalsIgnoreCase(getString(R.string.music)))
                    selectedItem = Multimedia.MUSIC;
                else if (selectedItem.equalsIgnoreCase(getString(R.string.books)))
                    selectedItem = Multimedia.BOOK;
                else if (selectedItem.equalsIgnoreCase(getString(R.string.serie)))
                    selectedItem = Multimedia.SERIE;

                adapter.clear();
                for (Multimedia media : listMediaBackup)
                    if (media.getType().equalsIgnoreCase(selectedItem))
                        adapter.add(media);
            }
            else if(criteria.equalsIgnoreCase(getString(R.string.original)))
            {
                adapter.clear();
                for (Multimedia media : listMediaBackup)
                    adapter.add(media);
            }
        }
        optionFilter = criteria;
    }

    private void openIntentBook(Book book)
    {
        Intent intent = new Intent(this,ViewMedia.class);
        if(book.getTitle().contains("("))
            book.setTitle(book.getTitle().substring(0,book.getTitle().indexOf("(")-1));
        if(book.getLanguage().equalsIgnoreCase("es"))
            book.setLanguage(getText(R.string.es).toString());
        else if(book.getLanguage().equalsIgnoreCase("en"))
            book.setLanguage(getText(R.string.en).toString());
        else if(book.getLanguage().equalsIgnoreCase("fr"))
            book.setLanguage(getText(R.string.fr).toString());
        else if(book.getLanguage().equalsIgnoreCase("de"))
            book.setLanguage(getText(R.string.de).toString());
        else if(book.getLanguage().equalsIgnoreCase("be"))
            book.setLanguage(getText(R.string.be).toString());
        intent.putExtra("media", book);
        startActivityForResult(intent, 2);
    }
    private void openIntentMusic(Music music)
    {
        Intent intent = new Intent(this,ViewMedia.class);
        if(music.getTitle().contains("("))
            music.setTitle(music.getTitle().substring(0,music.getTitle().indexOf("(")-1));
        intent.putExtra("media", music);
        startActivityForResult(intent, 2);
    }
    private void openIntentSerie(Serie serie)
    {
        Intent intent = new Intent(this,ViewMedia.class);
        if(serie.getTitle().contains("("))
            serie.setTitle(serie.getTitle().substring(0,serie.getTitle().indexOf("(")-1));
        intent.putExtra("media", serie);
        startActivityForResult(intent, 2);
    }
    private void openIntentMovie(Movie movie)
    {
        Intent intent = new Intent(this,ViewMedia.class);
        if(movie.getTitle().contains("("))
            movie.setTitle(movie.getTitle().substring(0,movie.getTitle().indexOf("(")-1));
        intent.putExtra("media", movie);
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 2)
        {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private int calcNecessaryWidth()
    {
        int extraSpace;
        DisplayMetrics display = getResources().getDisplayMetrics();
        int width;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
        {
            WindowMetrics windowMetrics = getWindowManager().getCurrentWindowMetrics();
            Insets insets = windowMetrics.getWindowInsets().getInsetsIgnoringVisibility(WindowInsets.Type.systemBars());
            width = windowMetrics.getBounds().width() - insets.left - insets.right;
        }
        else
        {
            getApplicationContext().getDisplay().getRealMetrics(display);
            width = display.widthPixels;
        }
        int widthSort = findViewById(R.id.iv_sortL_inside_list).getLayoutParams().width;
        int widthFilter = 0;
        int widthAdd = 0;
        if(!getIntent().getStringExtra("listTitle").equalsIgnoreCase(getString(R.string.music)) &&
                !getIntent().getStringExtra("listTitle").equalsIgnoreCase(getString(R.string.books)) &&
                !getIntent().getStringExtra("listTitle").equalsIgnoreCase(getString(R.string.movie)) &&
                !getIntent().getStringExtra("listTitle").equalsIgnoreCase(getString(R.string.serie)))
        {
            widthAdd = findViewById(R.id.iv_addL_inside_list).getLayoutParams().width;
            widthFilter = findViewById(R.id.im_filterL_inside_list).getLayoutParams().width;
        }
        int orientation = getResources().getConfiguration().orientation;
        extraSpace = (16*6 * (display.densityDpi / 160));
        if (orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            if (isInMultiWindowMode()) //Split Screen
            {
                extraSpace += (16 * 2 * (display.densityDpi / 160));
                width /= 2;
            }
            else
                extraSpace += (16 * 2 * (display.densityDpi / 160)) + (550 * (display.densityDpi / 160));

        }


        if(!isBasicList())
            return width-(widthAdd+widthFilter+widthSort+extraSpace);
        else
            return width-(widthFilter+widthSort+extraSpace);
    }
}
