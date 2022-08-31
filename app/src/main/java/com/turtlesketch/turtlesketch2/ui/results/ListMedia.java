package com.turtlesketch.turtlesketch2.ui.results;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
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
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.WindowMetrics;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.turtlesketch.turtlesketch2.Config;
import com.turtlesketch.turtlesketch2.Database;
import com.turtlesketch.turtlesketch2.MainActivity;
import com.turtlesketch.turtlesketch2.Multimedia.Book;
import com.turtlesketch.turtlesketch2.Multimedia.Movie;
import com.turtlesketch.turtlesketch2.Multimedia.Multimedia;
import com.turtlesketch.turtlesketch2.Multimedia.MultimediaSerializable;
import com.turtlesketch.turtlesketch2.Multimedia.Music;
import com.turtlesketch.turtlesketch2.Multimedia.Serie;
import com.turtlesketch.turtlesketch2.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * ListMedia Activity to show all the content of a query or a list.
 */
public class ListMedia extends AppCompatActivity
{
    /**
     * List of the media that is being shown on the Activity
     */
    List<Multimedia> listMedia = new ArrayList<>();
    /**
     * List with the original values of the media.
     */
    List<Multimedia> listMediaBackup = new ArrayList<>();
    /**
     * Adapter for the ListView component.
     */
    ArrayAdapterWithPhoto adapter;
    /**
     * Boolean to know if it's a list or a query the source of this Activity.
     */
    private boolean isList = false;
    /**
     * Option selected to filter the listMedia.
     */
    private String optionFilter;
    /**
     * Option selected to sort the listMedia.
     */
    private String optionSort;
    /**
     * SQLite Database
     */
    Database db;
    /**
     * Connection to SQLite Database
     */
    private SQLiteDatabase connection;

    /**
     * {@inheritDoc}
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        isList = getIntent().hasExtra("list");

        if(getIntent().hasExtra("media"))
        {
            if(Config.appSpanned)
            {
                setContentView(R.layout.view_list_media_dual);
            }
            else
            {
                setContentView(R.layout.view_list_media);
            }
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

        if(getIntent().hasExtra("sugg"))
            setTitle(getText(R.string.suggestions));

        if(isList)
            if(isBasicList())
            {
                findViewById(R.id.iv_addL_inside_list).setVisibility(View.INVISIBLE);
                findViewById(R.id.im_filterL_inside_list).setVisibility(View.INVISIBLE);
            }
        changeTheme(Config.theme);
        createConnection();
        createListener();
        if(isList && !isBasicList())
            createLongListListener();
    }

    private void createConnection()
    {
        db = new Database(this,"turtlesketch2.db", null, 3);
        connection = db.getWritableDatabase();
    }

    /**
     * Check if it's one the default for list that will behaviour different to the others.
     * @return True if it's one of that and false if not.
     */
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
            if(getIntent().hasExtra("addToList"))
                setTitle(getString(R.string.add_to) + " " + getIntent().getStringExtra("titleOfTheList"));
        }
    }

    /**
     * Check the icons to apply depending on the theme it's used by the user.
     */
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

    /**
     * Change the theme of the current Activity to match the system configuration or the selection of the User.
     * <br/><br/>
     * For the automatic configuration, it will depend on the system version we are running the App. If it's Android 10 or newer it will use the option included by Google.
     * In the case is Android 9 (Android Pie) it will use the battery saver to decide the theme of the application.
     * @param selectedText Actual theme selected by the user. If they never change it, auto will be the default option.
     * @author Pablo Oraa Lopez
     */
    private void changeTheme(@NotNull String selectedText)
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

    /**
     * Create and configure the listener for the Search bar, filter and sort options, and the click
     * on one of the items on the list.
     */
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
            createListenerForAdd();
        }
    }

    private void createLongListListener()
    {
        if(listMedia.size() > 0)
        {
            ((ListView)findViewById(R.id.lv_contentListQuery)).setOnItemLongClickListener((parent, view, position, id) ->
            {
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle(getText(R.string.delete_list_media));
                alert.setMessage(getText(R.string.delete_list_media_message));
                alert.setPositiveButton(getText(R.string.apply), (dialog, which) ->
                {
                    db.deleteListMedia(connection,getIntent().getStringExtra("listTitle"),listMedia.get(position).getId());
                    listMediaBackup.remove(listMedia.get(position));
                    adapter.remove(listMedia.get(position));
                });
                alert.setNegativeButton(getText(R.string.cancel), (dialog, which) -> dialog.dismiss());
                AlertDialog dialog = alert.create();
                dialog.show();
                return false;
            });
        }
    }


    private void createListenerForAdd()
    {
        findViewById(R.id.iv_addL_inside_list).setOnClickListener(v ->
        {
            List<Multimedia> allMedia = db.getAllData(connection);
            Intent intent = new Intent(this, ListMedia.class);
            if(!allMedia.isEmpty())
                intent.putExtra("media", new MultimediaSerializable(allMedia));
            else
                intent.putExtra("empty", true);
            //intent.putExtra("list", true);
            intent.putExtra("addToList", true);
            intent.putExtra("titleOfTheList", getIntent().getStringExtra("listTitle"));
            startActivity(intent);
        });
    }

    /**
     * Create the listener for the Search Bar with all the specific options and possibilities to do the query.
     */
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

    /**
     * Search on the list based on the query that is introduced into the search box.
     * @param query Text to search.
     */
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

    /**
     * Create the dialog to sort or filter the content of the list with the right content and options.
     * @param type Sort or filter depending on the selected option.
     */
    private void filterSortListener(@NotNull String type)
    {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.filter_sort_layout, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(view);
        if(type.equalsIgnoreCase("filter"))
        {
            filterChecked(view);
            alert.setTitle(getText(R.string.filter_list));
            alert.setMessage(getText(R.string.filter_list_message));
            createListenerForFuncFilter(view);
        }
        else if(type.equalsIgnoreCase("sort"))
        {
            sortChecked(view);
            alert.setTitle(getText(R.string.sort_list));
            alert.setMessage(getText(R.string.sort_list_message));
        }

        alert.setPositiveButton(getText(R.string.apply), (dialog, which) ->
        {
            if(type.equalsIgnoreCase("filter"))
            {
                filterList(view);
            }
            else if(type.equalsIgnoreCase("sort"))
            {
                sortList(view);
            }
        });
        alert.setNegativeButton(getText(R.string.cancel), (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = alert.create();
        dialog.show();
    }

    /**
     * Check the filter option once is selected by the user
     * @param view View that be used to get all possible content from the screen
     */
    private void filterList(@NotNull View view)
    {
        if(((RadioButton)view.findViewById(((RadioGroup) view.findViewById(R.id.rg_filter)).getCheckedRadioButtonId())).getText().toString().equalsIgnoreCase(getString(R.string.by_content_type)))
            setNewList(getString(R.string.by_content_type), ((Spinner)view.findViewById(R.id.sp_type_filter)).getSelectedItem().toString());
        else if(((RadioButton)view.findViewById(((RadioGroup) view.findViewById(R.id.rg_filter)).getCheckedRadioButtonId())).getText().toString().equalsIgnoreCase(getString(R.string.original)))
            setNewList(getString(R.string.original), null);
    }

    /**
     * Check the sort option once is selected by the user
     * @param view View that be used to get all possible content from the screen
     */
    private void sortList(@NotNull View view)
    {
        if(((RadioButton)view.findViewById(((RadioGroup) view.findViewById(R.id.rg_sort)).getCheckedRadioButtonId())).getText().toString().equalsIgnoreCase(getString(R.string.alphabetically)))
            setNewList(getString(R.string.alphabetically));
        else if(((RadioButton)view.findViewById(((RadioGroup) view.findViewById(R.id.rg_sort)).getCheckedRadioButtonId())).getText().toString().equalsIgnoreCase(getString(R.string.original)))
            setNewList(getString(R.string.original));
    }

    /**
     * Adapt the options to sort and filter based on the Activity in which we are and what is expected based on the list type.
     * @param view View that be used to het all possible content from the screen.
     */
    private void filterChecked(@NotNull View view)
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
        ((ConstraintLayout)view.findViewById(R.id.ln_filter_sort)).removeView(((ConstraintLayout)view.findViewById(R.id.ln_filter_sort)).getViewById(R.id.rg_sort));
    }

    /**
     * Adapt the options to sort and filter based on the Activity in which we are and what is expected based on the list type.
     * @param view View that be used to het all possible content from the screen.
     */
    private void sortChecked(@NotNull View view)
    {
        ((ConstraintLayout)view.findViewById(R.id.ln_filter_sort)).removeView(((ConstraintLayout)view.findViewById(R.id.ln_filter_sort)).getViewById(R.id.rg_filter));
        ((ConstraintLayout)view.findViewById(R.id.ln_filter_sort)).removeView(((ConstraintLayout)view.findViewById(R.id.ln_filter_sort)).getViewById(R.id.et_number_equals_less));
        ((ConstraintLayout)view.findViewById(R.id.ln_filter_sort)).removeView(((ConstraintLayout)view.findViewById(R.id.ln_filter_sort)).getViewById(R.id.et_number_equals));
        ((ConstraintLayout)view.findViewById(R.id.ln_filter_sort)).removeView(((ConstraintLayout)view.findViewById(R.id.ln_filter_sort)).getViewById(R.id.et_number_equals_more));
        ((ConstraintLayout)view.findViewById(R.id.ln_filter_sort)).removeView(((ConstraintLayout)view.findViewById(R.id.ln_filter_sort)).getViewById(R.id.sp_type_filter));
        for(int i = 0; i < ((RadioGroup)view.findViewById(R.id.rg_sort)).getChildCount();i++)
            if(!((RadioButton)((RadioGroup)view.findViewById(R.id.rg_sort)).getChildAt(i)).getText().toString().equalsIgnoreCase(getString(R.string.alphabetically)) &&
                    ! ((RadioButton)((RadioGroup)view.findViewById(R.id.rg_sort)).getChildAt(i)).getText().toString().equalsIgnoreCase(getString(R.string.original)))
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
    }

    /**
     * Create the activate/deactivate filter option based on the selected option by the user.
     * @param view View that be used to het all possible content from the screen.
     */
    private void createListenerForFuncFilter(@NotNull View view)
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

    /**
     * Method to sort the media list based on the possible values.
     * @param criteria Criteria to sort.
     */
    private void setNewList(String criteria) //For Sort
    {
        if(listMedia != null && listMedia.size() > 0)
            if(criteria.equalsIgnoreCase(getString(R.string.alphabetically)))
                sortAlphabetically();
            else if(criteria.equalsIgnoreCase(getString(R.string.original)))
                backToOriginal();
        optionSort = criteria;
    }

    private void sortAlphabetically()
    {
        for(int i = 0; i < listMedia.size();i++)
        {
            Multimedia media = listMedia.get(i);
            for(int j = i; j < listMedia.size();j++)
                if(media.getTitle().compareToIgnoreCase(listMedia.get(j).getTitle()) > 0)
                    media = swipeMedia(i,j,media);
        }
        List<Multimedia> newListMultimedia = new ArrayList<>(listMedia);
        adapter.clear();
        adapter.addAll(newListMultimedia);
    }

    private Multimedia swipeMedia(int i, int j, Multimedia media)
    {
        Multimedia newMedia = listMedia.get(j);
        listMedia.set(j,media);
        listMedia.set(i,newMedia);
        return newMedia;
    }

    private void backToOriginal()
    {
        adapter.clear();
        adapter.addAll(listMediaBackup);
    }

    /**
     * Method to filter the media list based on the possible values.
     * @param criteria Criteria to filter.
     * @param selectedItem In the case it's necessary a non pre-defined value, the value the user selected.
     */
    private void setNewList(String criteria, String selectedItem) //For filter
    {
        if(selectedItem != null)
        {
            if(listMediaBackup != null && listMediaBackup.size() > 0)
            {
                if (criteria.equalsIgnoreCase(getString(R.string.by_content_type)))
                    filterByType(selectedItem);
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
                for(Multimedia existingMedia : listMediaBackup)
                    if(media.getId().equalsIgnoreCase(existingMedia.getId()))
                        adapter.add(media);
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

    /**
     * Open the ViewMedia Activity with the media object being passed a Book, with the expected
     * personalized values for this object type.
     * @param book Book to be passed for the ViewMedia.
     */
    private void openIntentBook(@NotNull Book book)
    {
        Intent intent = new Intent(this,ViewMedia.class);
        if(book.getTitle().contains("("))
            book.setTitle(book.getTitle().substring(0,book.getTitle().indexOf("(")-1));
        if(book.getLanguage() != null)
        {
            if (book.getLanguage().equalsIgnoreCase("es"))
                book.setLanguage(getText(R.string.es).toString());
            else if (book.getLanguage().equalsIgnoreCase("en"))
                book.setLanguage(getText(R.string.en).toString());
            else if (book.getLanguage().equalsIgnoreCase("fr"))
                book.setLanguage(getText(R.string.fr).toString());
            else if (book.getLanguage().equalsIgnoreCase("de"))
                book.setLanguage(getText(R.string.de).toString());
            else if (book.getLanguage().equalsIgnoreCase("be"))
                book.setLanguage(getText(R.string.be).toString());
        }
        if(getIntent().hasExtra("addToList"))
            intent.putExtra("titleOfTheList", getIntent().getStringExtra("titleOfTheList"));
        intent.putExtra("media", book);
        startActivityForResult(intent, 2);
    }

    /**
     * Open the ViewMedia Activity with the media object being passed a song (Music(, with the expected
     * personalized values for this object type.
     * @param music song (Music) to be passed for the ViewMedia.
     */
    private void openIntentMusic(@NotNull Music music)
    {
        Intent intent = new Intent(this,ViewMedia.class);
        if(music.getTitle().contains("("))
            music.setTitle(music.getTitle().substring(0,music.getTitle().indexOf("(")-1));
        intent.putExtra("media", music);
        if(getIntent().hasExtra("addToList"))
            intent.putExtra("titleOfTheList", getIntent().getStringExtra("titleOfTheList"));
        startActivityForResult(intent, 2);
    }

    /**
     * Open the ViewMedia Activity with the media object being passed a Serie, with the expected
     * personalized values for this object type.
     * @param serie Serie to be passed for the ViewMedia.
     */
    private void openIntentSerie(@NotNull Serie serie)
    {
        Intent intent = new Intent(this,ViewMedia.class);
        if(serie.getTitle().contains("("))
            serie.setTitle(serie.getTitle().substring(0,serie.getTitle().indexOf("(")-1));
        intent.putExtra("media", serie);
        if(getIntent().hasExtra("addToList"))
            intent.putExtra("titleOfTheList", getIntent().getStringExtra("titleOfTheList"));
        startActivityForResult(intent, 2);
    }

    /**
     * Open the ViewMedia Activity with the media object being passed a Movie, with the expected
     * personalized values for this object type.
     * @param movie Movie to be passed for the ViewMedia.
     */
    private void openIntentMovie(@NotNull Movie movie)
    {
        Intent intent = new Intent(this,ViewMedia.class);
        if(movie.getTitle().contains("("))
            movie.setTitle(movie.getTitle().substring(0,movie.getTitle().indexOf("(")-1));
        intent.putExtra("media", movie);
        if(getIntent().hasExtra("addToList"))
            intent.putExtra("titleOfTheList", getIntent().getStringExtra("titleOfTheList"));
        startActivityForResult(intent, 2);
    }

    /**
     * {@inheritDoc}
     * <br/>
     * <ul>
     *     <li>If we receive 2 result code, it will go back to the main screen</li>
     *     <li>If we receive 3 result code, it will obtain and show the Suggestions for that type</li>
     *     <li>Any other case, it will keep the screen into the list of results.</li>
     * </ul>
     * @param requestCode Code expected that was defined during the startActivityForResult.
     * @param resultCode Current code receive by the new Activity.
     * @param data Intent with all information that has been passed.
     */
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
        else if(resultCode == 3)
        {
            Suggestions suggestions = new Suggestions();
            suggestions.showSuggestions(listMedia.get(0).getType(),this);
            Toast.makeText(this,getString(R.string.creating_suggestions),Toast.LENGTH_LONG).show();
            finish();
        }
    }

    /**
     * Calc all the necessary space that the top part of the screen will have and require to apply
     * to the Search bar given the fact that it's a dynamic component.
     * @return Pixels free to be used by the search bar.
     */
    private int calcNecessaryWidth()
    {
        int extraSpace;
        int width;
        int widthFilter = 0;
        int widthAdd = 0;
        int widthSort = findViewById(R.id.iv_sortL_inside_list).getLayoutParams().width;
        DisplayMetrics display = getResources().getDisplayMetrics();

        width = getWidth();
        if(!isBasicList())
        {
            widthAdd = findViewById(R.id.iv_addL_inside_list).getLayoutParams().width;
            widthFilter = findViewById(R.id.im_filterL_inside_list).getLayoutParams().width;
        }
        extraSpace = calcExtraSpace(display);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && isInMultiWindowMode())
            width /= 2;

        if(!isBasicList())
            return width-(widthAdd+widthFilter+widthSort+extraSpace);
        else
            return width-(widthFilter+widthSort+extraSpace);
    }

    /**
     * Check and return the width of the display in the current mode of the phone. If it's Android 11,
     * it will use the new way to obtain it, but in older versions use the traditional way which won't have any problem.
     * @return Number of pixels which occupies the width screen.
     */
    private int getWidth()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
        {
            WindowMetrics windowMetrics = getWindowManager().getCurrentWindowMetrics();
            Insets insets = windowMetrics.getWindowInsets().getInsetsIgnoringVisibility(WindowInsets.Type.systemBars());
            return windowMetrics.getBounds().width() - insets.left - insets.right;
        }
        else
        {
            WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            Display realDisplay = wm.getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            realDisplay.getMetrics(metrics);
            return metrics.widthPixels;
        }
    }

    /**
     * Calc the space that is necessary to remove from the search bar depending on the spaces, orientation and Application mode.
     * <br/>
     * By default it will return (16*6 * (display.densityDpi / 160)), as on every mode tested, portrait mode will be visible without
     * problem on this mode. In the case is Landscape mode, it will add + more separations to the extra default space, and in the case
     * the Activity is full screen it will add more space just to kepp everything in the desire position.
     * @param display Screen of the application with all the sizes.
     * @return Number of pixels to remove from the search bar depending on the screen mode.
     */
    private int calcExtraSpace(@NotNull DisplayMetrics display)
    {
        int extraSpace;
        int orientation = getResources().getConfiguration().orientation;
        extraSpace = (16*6 * (display.densityDpi / 160));
        if (orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            if (isInMultiWindowMode()) //Split Screen
                extraSpace += (16 * 2 * (display.densityDpi / 160));
            else
                extraSpace += (16 * 2 * (display.densityDpi / 160)) + (550 * (display.densityDpi / 160));

        }
        return extraSpace;
    }
}
