package com.turtlesketch.turtlesketch2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.ArraySet;

import com.turtlesketch.turtlesketch2.Multimedia.Book;
import com.turtlesketch.turtlesketch2.Multimedia.Movie;
import com.turtlesketch.turtlesketch2.Multimedia.Multimedia;
import com.turtlesketch.turtlesketch2.Multimedia.Music;
import com.turtlesketch.turtlesketch2.Multimedia.Serie;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Database extends SQLiteOpenHelper
{
    public Database(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, name, factory, version);
    }

    /**
     * {@inheritDoc}
     * <br/>
     * Tables to create are:
     * <ul>
     *     <li>Books</li>
     *     <li>Music</li>
     *     <li>Serie</li>
     *     <li>Movie</li>
     *     <li>List</li>
     * </ul>
     *
     * @param sqLiteDatabase Database object which will contain all the necessary methods to interact with the database we created on the Constructor.
     */
    @Override
    public void onCreate(@NotNull SQLiteDatabase sqLiteDatabase)
    {
        String tableCreation =  "CREATE TABLE BOOKS" +
                "(" +
                "id  VARCHAR(8) PRIMARY KEY," +
                "title VARCHAR(50) NOT NULL," +
                "author VARCHAR(50) NOT NULL," +
                "publisher VARCHAR(50)," +
                "plot VARCHAR(200)," +
                "category VARCHAR(50)," +
                "cover VARCHAR(2083)," +
                "lang VARCHAR(30)," +
                "publishDate VARCHAR(50)" +
                ");";
        sqLiteDatabase.execSQL(tableCreation);
        tableCreation = "CREATE TABLE MUSIC" +
                "(" +
                "id  VARCHAR(8) PRIMARY KEY," +
                "title VARCHAR(50) NOT NULL," +
                "artist VARCHAR(50) NOT NULL," +
                "publisher VARCHAR(50)," +
                "duration VARCHAR(200) NOT NULL," +
                "cover VARCHAR(2083)," +
                "album VARCHAR(50)," +
                "lang VARCHAR(30)," +
                "publishDate VARCHAR(50)," +
                "description VARCHAR(2048)," +
                "gender VARCHAR(50)" +
                ");";
        sqLiteDatabase.execSQL(tableCreation);
        tableCreation = "CREATE TABLE SERIE" +
                "(" +
                "id  VARCHAR(8) PRIMARY KEY," +
                "title VARCHAR(50) NOT NULL," +
                "seasonNumber VARCHAR(10)," +
                "plot VARCHAR(200)," +
                "country VARCHAR(20)," +
                "director VARCHAR(50)," +
                "actors VARCHAR(300)," +
                "durationPerEpisode VARCHAR(200)," +
                "cover VARCHAR(2083)," +
                "lang VARCHAR(30)," +
                "publishDate VARCHAR(50)," +
                "gender VARCHAR(50)" +
                ");";
        sqLiteDatabase.execSQL(tableCreation);
        tableCreation = "CREATE TABLE MOVIE" +
                "(" +
                "id  VARCHAR(8) PRIMARY KEY," +
                "title VARCHAR(50) NOT NULL," +
                "director VARCHAR(50)," +
                "actors VARCHAR(200)," +
                "plot VARCHAR(200)," +
                "duration VARCHAR(200) NOT NULL," +
                "cover VARCHAR(2083)," +
                "lang VARCHAR(30)," +
                "publishDate VARCHAR(50) NOT NULL," +
                "gender VARCHAR(50) NOT NULL" +
                ");";
        sqLiteDatabase.execSQL(tableCreation);

        tableCreation = "CREATE TABLE LIST" +
                "(" +
                "title VARCHAR(50) NOT NULL," +
                "media VARCHAR(50) NOT NULL," +
                "PRIMARY KEY(title, media)" +
                ");";
        sqLiteDatabase.execSQL(tableCreation);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {

    }

    /**
     * Insert the media object in the table defined by it's type.
     * @param sqLiteDatabase SQLiteDatabase object to interact with the database created on the constructor.
     * @param media Multimedia object which will contain all the necessary data for the app to work as expected.
     * @return true if the insert is done succesfully and false is there was an error.
     */
    public boolean insertMultimedia(@NotNull SQLiteDatabase sqLiteDatabase, @NotNull Multimedia media)
    {
        long result = sqLiteDatabase.insert(media.getType(),null,media.getContentValues());
        return result != -1;
    }

    /**
     * Insert media object into one specific list different to the type one.
     * @param sqLiteDatabase SQLiteDatabase object to interact with the database created on the constructor.
     * @param media Multimedia object which will contain all the necessary data for the app to work as expected.
     * @param titleOfTheList List in which the media will be inserted.
     * @return true if the insert is done succesfully and false is there was an error.
     */
    public boolean insertIntoList(@NotNull SQLiteDatabase sqLiteDatabase, @NotNull Multimedia media, String titleOfTheList)
    {
        ContentValues content = new ContentValues();
        content.put("title", titleOfTheList);
        content.put("media", media.getId());
        long result = sqLiteDatabase.insert("List",null,content);
        return result != -1;
    }

    /**
     * Retrieves a list of multimedia object in the case we want to know an specific value of a specific table.
     * @param sqLiteDatabase SQLiteDatabase object to interact with the database created on the constructor.
     * @param title Title of the media object to search.
     * @param table Table to search that title.
     * @return List with all possible media object with that title.
     */
    public List<Multimedia> selectMultimedia(SQLiteDatabase sqLiteDatabase, String title, @NotNull String table)
    {
        List<Multimedia> media = new ArrayList<>();
        Cursor c;
        switch (table)
        {
            case Multimedia.BOOK:
                c = sqLiteDatabase.rawQuery("SELECT * FROM BOOKS WHERE title = ?", new String[]{title});
                media.add(new Book());
                break;
            case Multimedia.MOVIE:
                c = sqLiteDatabase.rawQuery("SELECT * FROM MOVIE WHERE title = ?", new String[]{title});
                media.add(new Movie());
                break;
            case Multimedia.SERIE:
                c = sqLiteDatabase.rawQuery("SELECT * FROM SERIE WHERE title = ?", new String[]{title});
                media.add(new Serie());
                break;
            case Multimedia.MUSIC:
                c = sqLiteDatabase.rawQuery("SELECT * FROM MUSIC WHERE title = ?", new String[]{title});
                media.add(new Music());
                break;
            default:
                c = sqLiteDatabase.rawQuery("SELECT * FROM MOVIE mo, BOOKS b, SERIE s, MUSIC mu WHERE mo.title = ? OR b.title = ? OR s.title = ? OR mu.title = ?", new String[]{title,title,title,title});
                media.add(new Movie());
                break;
        }

        if(c != null) c.close();
        return media;
    }

    /**
     * Retrieves all the content stored in a list that exist on the database.
     * @param sqLiteDatabase SQLiteDatabase object to interact with the database created on the constructor.
     * @param titleOfTheList Title of the list to get all content.
     * @return List with all the Multimedia objects that were found.
     */
    public List<Multimedia> selectList(SQLiteDatabase sqLiteDatabase, String titleOfTheList)
    {
        List<Multimedia> media = new ArrayList<>();
        List<String> listOfIds = getIds(sqLiteDatabase,titleOfTheList);
        String options = "(";
        if(listOfIds == null)
            return media;
        for(String id: listOfIds)
            options = options.concat("'" + id + "',");
        options = options.substring(0,options.length()-1).concat(")");

        Cursor c = sqLiteDatabase.rawQuery("SELECT * " +
                                                "FROM MOVIE " +
                                                "WHERE id IN "+options , null);
        while(c.moveToNext())
            media.add(new Movie(c.getString(c.getColumnIndex("id")),c.getString(c.getColumnIndex("title")),
                    c.getString(c.getColumnIndex("actors")),c.getString(c.getColumnIndex("publishDate")),c.getString(c.getColumnIndex("gender")),
                    c.getString(c.getColumnIndex("lang")),c.getString(c.getColumnIndex("cover")),"",
                    c.getString(c.getColumnIndex("plot")),c.getString(c.getColumnIndex("director")),c.getString(c.getColumnIndex("duration"))));
        c = sqLiteDatabase.rawQuery("SELECT * " +
                                         "FROM BOOKS " +
                                         "WHERE id IN "+options , null);
        while(c.moveToNext())
            media.add(new Book(c.getString(c.getColumnIndex("id")),c.getString(c.getColumnIndex("title")),
                    c.getString(c.getColumnIndex("author")),c.getString(c.getColumnIndex("publishDate")),c.getString(c.getColumnIndex("category")),
                    c.getString(c.getColumnIndex("lang")),c.getString(c.getColumnIndex("cover")),"",
                    c.getString(c.getColumnIndex("plot")),c.getString(c.getColumnIndex("publisher"))));
        c = sqLiteDatabase.rawQuery("SELECT * " +
                                        "FROM MUSIC " +
                                        "WHERE id IN "+options , null);
        while(c.moveToNext())
            media.add(new Music(c.getString(c.getColumnIndex("id")),c.getString(c.getColumnIndex("title")),
                    c.getString(c.getColumnIndex("artist")),c.getString(c.getColumnIndex("publishDate")),c.getString(c.getColumnIndex("gender")),
                    c.getString(c.getColumnIndex("lang")),c.getString(c.getColumnIndex("cover")),"",
                    c.getString(c.getColumnIndex("duration")),c.getString(c.getColumnIndex("publisher")),c.getString(c.getColumnIndex("description"))));
        c = sqLiteDatabase.rawQuery("SELECT * " +
                                        "FROM SERIE " +
                                        "WHERE id IN "+options , null);
        while(c.moveToNext())
            media.add(new Serie(c.getString(c.getColumnIndex("id")),c.getString(c.getColumnIndex("title")),
                    c.getString(c.getColumnIndex("actors")),c.getString(c.getColumnIndex("publishDate")),c.getString(c.getColumnIndex("gender")),
                    c.getString(c.getColumnIndex("lang")),c.getString(c.getColumnIndex("cover")),"",
                    c.getString(c.getColumnIndex("plot")),c.getString(c.getColumnIndex("director")),c.getString(c.getColumnIndex("country")),
                    c.getString(c.getColumnIndex("durationPerEpisode")),c.getString(c.getColumnIndex("seasonNumber"))));

        c.close();
        return media;
    }

    /**
     * Get all names of the lists that are stored in the Database.
     * @param sqLiteDatabase SQLiteDatabase object to interact with the database created on the constructor.
     * @return List of String with all the different names existing on the database.
     */
    public List<String> selectListsNames(@NotNull SQLiteDatabase sqLiteDatabase)
    {
        Set<String> nameList = new ArraySet<>();
        Cursor c = sqLiteDatabase.query("Lists", new String[] {"title"},null,null,null,null,null);
        while(c.moveToNext())
             nameList.add(c.getString(c.getColumnIndex("title")));
        c.close();
        return new ArrayList<>(nameList);
    }

    /**
     * Get all the Ids of the media objects that are contained on a specific list. If it's one of the four main lists it will retrieve all data of that specific type.
     * @param sqLiteDatabase SQLiteDatabase object to interact with the database created on the constructor.
     * @param titleOfTheList Title of the list to get the Ids.
     * @return List of String with the different Ids.
     */
    @Nullable
    private List<String> getIds(SQLiteDatabase sqLiteDatabase, @NotNull String titleOfTheList)
    {
        List<String> listOfIds = new ArrayList<>();
        Cursor c;
        switch (titleOfTheList) {
            case Multimedia.BOOK:
                c = sqLiteDatabase.rawQuery("SELECT * " +
                        "FROM BOOKS ", null);
                while (c.moveToNext())
                    listOfIds.add(c.getString(c.getColumnIndex("id")));
                break;
            case Multimedia.MUSIC:
                c = sqLiteDatabase.rawQuery("SELECT * " +
                        "FROM MUSIC ", null);
                while (c.moveToNext())
                    listOfIds.add(c.getString(c.getColumnIndex("id")));
                break;
            case Multimedia.MOVIE:
                c = sqLiteDatabase.rawQuery("SELECT * " +
                        "FROM MOVIE ", null);
                while (c.moveToNext())
                    listOfIds.add(c.getString(c.getColumnIndex("id")));
                break;
            case Multimedia.SERIE:
                c = sqLiteDatabase.rawQuery("SELECT * " +
                        "FROM SERIE ", null);
                while (c.moveToNext())
                    listOfIds.add(c.getString(c.getColumnIndex("id")));
                break;
            default:
                c = sqLiteDatabase.rawQuery("SELECT media " +
                        "FROM LIST " +
                        "WHERE title = ?", new String[]{titleOfTheList});
                while (c.moveToNext())
                    listOfIds.add(c.getString(c.getColumnIndex("media")));
                break;
        }
        c.close();
        if(listOfIds.size() > 0)
            return listOfIds;
        else return null;
    }

    /**
     * Check if a media exists right now in the database.
     * @param sqLiteDatabase SQLiteDatabase object to interact with the database created on the constructor.
     * @param title Title to search for.
     * @return True if the media object exists and false if not.
     */
    public boolean existsMultimedia(@NotNull SQLiteDatabase sqLiteDatabase, String title)
    {
        Cursor c = sqLiteDatabase.rawQuery("SELECT * " +
                "FROM MOVIE " +
                "WHERE title = ?" , new String[] {title});
        if(c.moveToNext())
        {
            c.close();
            return true;
        }
        c = sqLiteDatabase.rawQuery("SELECT * " +
                "FROM BOOKS b " +
                "WHERE title = ?" , new String[] {title});
        if(c.moveToNext())
        {
            c.close();
            return true;
        }
        c = sqLiteDatabase.rawQuery("SELECT * " +
                "FROM MUSIC " +
                "WHERE title = ?" , new String[] {title});
        if(c.moveToNext())
        {
            c.close();
            return true;
        }
        c = sqLiteDatabase.rawQuery("SELECT * " +
                "FROM SERIE " +
                "WHERE title = ?" , new String[] {title});
        if(c.moveToNext())
        {
            c.close();
            return true;
        }
        c.close();
        return false;
    }

    /**
     * Check if a media exists right now in the list asked by the user.
     * @param sqLiteDatabase SQLiteDatabase object to interact with the database created on the constructor.
     * @param id Id of the object to search for it in the list.
     * @param titleOfTheList List in which we have to look for the ID.
     * @return True if exists and false if not.
     */
    public boolean existsMultimediaIntoList(@NotNull SQLiteDatabase sqLiteDatabase, String id, String titleOfTheList)
    {
        Cursor c = sqLiteDatabase.rawQuery("SELECT * " +
                                                "FROM LIST " +
                                                "WHERE media = ?1 " +
                                                "AND title = ?2", new String[]{id,titleOfTheList});
        if(c.moveToNext())
        {
            c.close();
            return true;
        }
        c.close();
        return false;
    }

    /**
     * Get the number of media objects stored into one of the list existing in the user app.
     * @param sqLiteDatabase SQLiteDatabase object to interact with the database created on the constructor.
     * @param listName Name of the list to get the count.
     * @return Number of media objects in that list.
     */
    public int getNumberOfContentOfAList(SQLiteDatabase sqLiteDatabase, @NotNull String listName)
    {
        long count;
        switch (listName) {
            case Multimedia.BOOK:
                count = DatabaseUtils.queryNumEntries(sqLiteDatabase, "BOOKS");
                break;
            case Multimedia.MUSIC:
                count = DatabaseUtils.queryNumEntries(sqLiteDatabase, "MUSIC");
                break;
            case Multimedia.MOVIE:
                count = DatabaseUtils.queryNumEntries(sqLiteDatabase, "MOVIE");
                break;
            case Multimedia.SERIE:
                count = DatabaseUtils.queryNumEntries(sqLiteDatabase, "SERIE");
                break;
            default:
                count = DatabaseUtils.queryNumEntries(sqLiteDatabase, "LIST", "title = ?", new String[] {listName});
                break;
        }
        return (int) count;
    }

    /**
     * Delete one list from the database.
     * @param sqLiteDatabase SQLiteDatabase object to interact with the database created on the constructor.
     * @param listName List to be deleted.
     */
    public void deleteList(@NotNull SQLiteDatabase sqLiteDatabase, String listName)
    {
        sqLiteDatabase.delete("LIST", "title = ?", new String[]{listName});
    }

    /**
     * Delete a media object from one of the list of the database. It's impossible that users delete it from the four basic lists, so it will only check the table List.
     * @param sqLiteDatabase SQLiteDatabase object to interact with the database created on the constructor.
     * @param listName Name of the list to delete the object from the database.
     * @param mediaID Object to be deleted.
     */
    public void deleteListMedia(@NotNull SQLiteDatabase sqLiteDatabase, String listName, String mediaID)
    {
        sqLiteDatabase.delete("LIST", "title = ? AND media = ?", new String[] {listName, mediaID});
    }

    /**
     *
     * @param sqLiteDatabase SQLiteDatabase object to interact with the database created on the constructor.
     * @return Most typical category of the user in the books table.
     */
    public String getMostTypicalCategories(@NotNull SQLiteDatabase sqLiteDatabase)
    {
        String category = "";

        Cursor c = sqLiteDatabase.rawQuery("SELECT category FROM BOOKS GROUP BY Category ORDER BY COUNT(Category) DESC", null);
        if(c.moveToNext())
            category = (c.getString(c.getColumnIndex("category")));
        c.close();
        return category;
    }

    public List<Multimedia> getAllData(SQLiteDatabase sqLiteDatabase)
    {
        List<Multimedia> media = new ArrayList<>();

        Cursor c = sqLiteDatabase.rawQuery("SELECT * " +
                "FROM MOVIE " , null);
        while(c.moveToNext())
            media.add(new Movie(c.getString(c.getColumnIndex("id")),c.getString(c.getColumnIndex("title")),
                    c.getString(c.getColumnIndex("actors")),c.getString(c.getColumnIndex("publishDate")),c.getString(c.getColumnIndex("gender")),
                    c.getString(c.getColumnIndex("lang")),c.getString(c.getColumnIndex("cover")),"",
                    c.getString(c.getColumnIndex("plot")),c.getString(c.getColumnIndex("director")),c.getString(c.getColumnIndex("duration"))));
        c = sqLiteDatabase.rawQuery("SELECT * " +
                "FROM BOOKS " , null);
        while(c.moveToNext())
            media.add(new Book(c.getString(c.getColumnIndex("id")),c.getString(c.getColumnIndex("title")),
                    c.getString(c.getColumnIndex("author")),c.getString(c.getColumnIndex("publishDate")),c.getString(c.getColumnIndex("category")),
                    c.getString(c.getColumnIndex("lang")),c.getString(c.getColumnIndex("cover")),"",
                    c.getString(c.getColumnIndex("plot")),c.getString(c.getColumnIndex("publisher"))));
        c = sqLiteDatabase.rawQuery("SELECT * " +
                "FROM MUSIC " , null);
        while(c.moveToNext())
            media.add(new Music(c.getString(c.getColumnIndex("id")),c.getString(c.getColumnIndex("title")),
                    c.getString(c.getColumnIndex("artist")),c.getString(c.getColumnIndex("publishDate")),c.getString(c.getColumnIndex("gender")),
                    c.getString(c.getColumnIndex("lang")),c.getString(c.getColumnIndex("cover")),"",
                    c.getString(c.getColumnIndex("duration")),c.getString(c.getColumnIndex("publisher")),c.getString(c.getColumnIndex("description"))));
        c = sqLiteDatabase.rawQuery("SELECT * " +
                "FROM SERIE ", null);
        while(c.moveToNext())
            media.add(new Serie(c.getString(c.getColumnIndex("id")),c.getString(c.getColumnIndex("title")),
                    c.getString(c.getColumnIndex("actors")),c.getString(c.getColumnIndex("publishDate")),c.getString(c.getColumnIndex("gender")),
                    c.getString(c.getColumnIndex("lang")),c.getString(c.getColumnIndex("cover")),"",
                    c.getString(c.getColumnIndex("plot")),c.getString(c.getColumnIndex("director")),c.getString(c.getColumnIndex("country")),
                    c.getString(c.getColumnIndex("durationPerEpisode")),c.getString(c.getColumnIndex("seasonNumber"))));

        c.close();
        return media;
    }
}
