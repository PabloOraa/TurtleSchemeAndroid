package com.turtlesketch.turtlesketch;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.turtlesketch.turtlesketch.Multimedia.Book;
import com.turtlesketch.turtlesketch.Multimedia.Movie;
import com.turtlesketch.turtlesketch.Multimedia.Multimedia;
import com.turtlesketch.turtlesketch.Multimedia.Music;
import com.turtlesketch.turtlesketch.Multimedia.Serie;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper
{
    public Database(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
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

    public boolean insertMultimedia(SQLiteDatabase sqLiteDatabase, Multimedia media)
    {
        long result = sqLiteDatabase.insert(media.getType(),null,media.getContentValues());
        return result != -1;
    }

    public boolean insertIntoList(SQLiteDatabase sqLiteDatabase, Multimedia media, String titleOfTheList)
    {
        ContentValues content = new ContentValues();
        content.put("title", titleOfTheList);
        content.put("media", media.getId());
        long result = sqLiteDatabase.insert("List",null,content);
        return result != -1;
    }

    public List<Multimedia> selectMultimedia(SQLiteDatabase sqLiteDatabase, String title, String table)
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

    public List<String> selectListsNames(SQLiteDatabase sqLiteDatabase)
    {
        List<String> nameList = new ArrayList<>();
        Cursor c = sqLiteDatabase.query("Lists", new String[] {"title"},null,null,null,null,null);
        while(c.moveToNext())
             nameList.add(c.getString(c.getColumnIndex("title")));
        c.close();
        return nameList;
    }

    private List<String> getIds(SQLiteDatabase sqLiteDatabase, String titleOfTheList)
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

    public boolean existsMultimedia(SQLiteDatabase sqLiteDatabase, String title)
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

    public int getNumberOfContentOfAList(SQLiteDatabase sqLiteDatabase, String listName)
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

    public int deleteList(SQLiteDatabase sqLiteDatabase, String listName)
    {
        return sqLiteDatabase.delete("LIST", "title = ?", new String[] {listName});
    }
}
