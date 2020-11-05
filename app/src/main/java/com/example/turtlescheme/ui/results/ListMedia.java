package com.example.turtlescheme.ui.results;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.turtlescheme.Config;
import com.example.turtlescheme.MainActivity;
import com.example.turtlescheme.Multimedia.Book;
import com.example.turtlescheme.Multimedia.Movie;
import com.example.turtlescheme.Multimedia.Multimedia;
import com.example.turtlescheme.Multimedia.MultimediaSerializable;
import com.example.turtlescheme.Multimedia.Music;
import com.example.turtlescheme.Multimedia.Serie;
import com.example.turtlescheme.R;

import java.util.ArrayList;
import java.util.List;

public class ListMedia extends AppCompatActivity
{
    List<Multimedia> listMedia = new ArrayList<>();
    List<Multimedia> listMediaBackup = new ArrayList<>();
    ArrayAdapterWithPhoto adapter;
    private boolean isList = false;

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
            if(getIntent().getStringExtra("listTitle").equalsIgnoreCase(getString(R.string.music)) ||
                    getIntent().getStringExtra("listTitle").equalsIgnoreCase(getString(R.string.books)) ||
                    getIntent().getStringExtra("listTitle").equalsIgnoreCase(getString(R.string.movie)) ||
                    getIntent().getStringExtra("listTitle").equalsIgnoreCase(getString(R.string.serie)))
            {
                findViewById(R.id.iv_addL_inside_list).setVisibility(View.INVISIBLE);
                findViewById(R.id.im_filterL_inside_list).setVisibility(View.INVISIBLE);
            }
        }
        changeTheme(Config.theme);
        createListener();
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        if(isList)
        {
            int totalWidth = calcNecessaryWidth();
            ViewGroup.LayoutParams layoutParams = findViewById(R.id.sv_searchL_inside_list).getLayoutParams();
            layoutParams.width = totalWidth;
            findViewById(R.id.sv_searchL_inside_list).setLayoutParams(layoutParams);
        }
        else
        {
            if(findViewById(R.id.cl_list_inside_list) != null)
                ((LinearLayout)findViewById(R.id.cl_list_inside_list).getParent()).removeView(findViewById(R.id.cl_list_inside_list));
            //findViewById(R.id.cl_list_inside_list).setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
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
            if(findViewById(R.id.iv_sortL) != null)
                findViewById(R.id.iv_sortL).setOnClickListener(v -> filterSortListener("sort"));
            else if(findViewById(R.id.iv_sortL_inside_list) != null)
                findViewById(R.id.iv_sortL_inside_list).setOnClickListener(v -> filterSortListener("sort"));

            if(findViewById(R.id.im_filterL) != null)
                findViewById(R.id.im_filterL).setOnClickListener(v -> filterSortListener("filter"));
            else if(findViewById(R.id.im_filterL_inside_list) != null)
                findViewById(R.id.im_filterL_inside_list).setOnClickListener(v -> filterSortListener("filter"));
        }
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
                    if(!((RadioButton)((RadioGroup)view.findViewById(R.id.rg_filter)).getChildAt(i)).getText().toString().equalsIgnoreCase(getString(R.string.by_content_type)))
                        ((ConstraintLayout) view.findViewById(R.id.ln_filter_sort)).removeView(((RadioGroup) view.findViewById(R.id.rg_filter)).getChildAt(i));
            }
            //((LinearLayout)view.findViewById(R.id.ln_filter_sort)).removeView(findViewById(R.id.rg_sort));
            ((ConstraintLayout)view.findViewById(R.id.ln_filter_sort)).removeView(((ConstraintLayout)view.findViewById(R.id.ln_filter_sort)).getViewById(R.id.rg_sort));
            alert.setTitle(getText(R.string.add_list));
            alert.setMessage(getText(R.string.add_list_message));
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
        if(view.findViewById(R.id.rg_filter) != null)
        {
            ((RadioGroup)view.findViewById(R.id.rg_filter)).setOnCheckedChangeListener((group, checkedId) ->
            {
                if(((RadioButton)view.findViewById(checkedId)).getText().toString().equalsIgnoreCase(getString(R.string.by_content_type)))
                {
                    view.findViewById(R.id.sp_type_filter).setEnabled(true);
                    /*view.findViewById(R.id.et_number_equals_more).setEnabled(false);
                    view.findViewById(R.id.et_number_equals).setEnabled(false);
                    view.findViewById(R.id.et_number_equals_less).setEnabled(false);*/
                }
                else if(((RadioButton)view.findViewById(checkedId)).getText().toString().equalsIgnoreCase(getString(R.string.by_number_equal_more)))
                {
                    view.findViewById(R.id.sp_type_filter).setEnabled(false);
                    /*view.findViewById(R.id.et_number_equals_more).setEnabled(true);
                    view.findViewById(R.id.et_number_equals).setEnabled(false);
                    view.findViewById(R.id.et_number_equals_less).setEnabled(false);*/
                }
                else if(((RadioButton)view.findViewById(checkedId)).getText().toString().equalsIgnoreCase(getString(R.string.by_number_equal_less)))
                {
                    view.findViewById(R.id.sp_type_filter).setEnabled(false);
                    /*view.findViewById(R.id.et_number_equals_more).setEnabled(false);
                    view.findViewById(R.id.et_number_equals).setEnabled(false);
                    view.findViewById(R.id.et_number_equals_less).setEnabled(true);*/
                }
                else if(((RadioButton)view.findViewById(checkedId)).getText().toString().equalsIgnoreCase(getString(R.string.by_number_equals)))
                {
                    view.findViewById(R.id.sp_type_filter).setEnabled(false);
                    /*view.findViewById(R.id.et_number_equals_more).setEnabled(true);
                    view.findViewById(R.id.et_number_equals).setEnabled(true);
                    view.findViewById(R.id.et_number_equals_less).setEnabled(false);*/
                }
            });
            view.findViewById(R.id.sp_type_filter).setEnabled(false);
            /*view.findViewById(R.id.et_number_equals_more).setEnabled(false);
            view.findViewById(R.id.et_number_equals).setEnabled(false);
            view.findViewById(R.id.et_number_equals_less).setEnabled(false);*/
        }
    }

    private void setNewList(String criteria, String selectedItem)
    {
        if(criteria.equalsIgnoreCase("type"))
        {
            if(selectedItem.equalsIgnoreCase(getString(R.string.movie)))
                selectedItem = Multimedia.MOVIE;
            else if(selectedItem.equalsIgnoreCase(getString(R.string.music)))
                selectedItem = Multimedia.MUSIC;
            else if(selectedItem.equalsIgnoreCase(getString(R.string.books)))
                selectedItem = Multimedia.BOOK;
            else if(selectedItem.equalsIgnoreCase(getString(R.string.serie)))
                selectedItem = Multimedia.SERIE;

            adapter.clear();
            for(Multimedia media : listMediaBackup)
                if(media.getType().equalsIgnoreCase(selectedItem))
                    adapter.add(media);
        }
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
        getWindowManager().getDefaultDisplay().getMetrics(display);
        width = display.widthPixels;
        //}
        int widthSort = findViewById(R.id.iv_sortL_inside_list).getLayoutParams().width;
        int widthFilter = findViewById(R.id.im_filterL_inside_list).getLayoutParams().width;
        int widthAdd = 0;
        String text = getIntent().getStringExtra("listTitle");
        if(!getIntent().getStringExtra("listTitle").equalsIgnoreCase(getString(R.string.music)) &&
                !getIntent().getStringExtra("listTitle").equalsIgnoreCase(getString(R.string.books)) &&
                !getIntent().getStringExtra("listTitle").equalsIgnoreCase(getString(R.string.movie)) &&
                !getIntent().getStringExtra("listTitle").equalsIgnoreCase(getString(R.string.serie)))
            widthAdd = findViewById(R.id.iv_addL_inside_list).getLayoutParams().width;
        int orientation = getResources().getConfiguration().orientation;
        /*if (orientation == Configuration.ORIENTATION_LANDSCAPE)
            extraSpace = (16*6 * (display.densityDpi / 160)) + (550 * (display.densityDpi / 160));
        else*/
            extraSpace = (16*6 * (display.densityDpi / 160));

        if(!getIntent().getStringExtra("listTitle").equalsIgnoreCase(getString(R.string.music)) &&
                !getIntent().getStringExtra("listTitle").equalsIgnoreCase(getString(R.string.books)) &&
                !getIntent().getStringExtra("listTitle").equalsIgnoreCase(getString(R.string.movie)) &&
                !getIntent().getStringExtra("listTitle").equalsIgnoreCase(getString(R.string.serie)))
            return width-(widthAdd+widthFilter+widthSort+extraSpace);
        else
            return width-(widthFilter+widthSort+extraSpace);
    }
}
