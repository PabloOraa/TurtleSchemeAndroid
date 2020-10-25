package com.example.turtlescheme;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.turtlescheme.Multimedia.Book;
import com.example.turtlescheme.Multimedia.Movie;
import com.example.turtlescheme.Multimedia.Multimedia;
import com.example.turtlescheme.Multimedia.Music;
import com.example.turtlescheme.Multimedia.Serie;

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
                "plot VARCHAR(200) NOT NULL," +
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
                "gender VARCHAR(50)" +
                ");";
        sqLiteDatabase.execSQL(tableCreation);
        tableCreation = "CREATE TABLE SERIE" +
                "(" +
                "id  VARCHAR(8) PRIMARY KEY," +
                "title VARCHAR(50) NOT NULL," +
                "seasonNumber INTEGER," +
                "chapterNumber INTEGER," +
                "channel VARCHAR(50)," +
                "plot VARCHAR(200)," +
                "country VARCHAR(20)," +
                "director VARCHAR(50)," +
                "actors VARCHAR(300)," +
                "durationPerEpisode VARCHAR(200) NOT NULL," +
                "cover VARCHAR(2083)," +
                "lang VARCHAR(30)," +
                "publishDate VARCHAR(50) NOT NULL," +
                "gender VARCHAR(50) NOT NULL" +
                ");";
        sqLiteDatabase.execSQL(tableCreation);
        tableCreation = "CREATE TABLE MOVIE" +
                "(" +
                "id  VARCHAR(8) PRIMARY KEY," +
                "title VARCHAR(50) NOT NULL," +
                "director VARCHAR(50)," +
                "actors VARCHAR(200)," +
                "plot VARCHAR(200)," +
                "publisher VARCHAR(50)," +
                "duration VARCHAR(200) NOT NULL," +
                "cover VARCHAR(2083)," +
                "lang VARCHAR(30)," +
                "publishDate VARCHAR(50) NOT NULL," +
                "gender VARCHAR(50) NOT NULL" +
                ");";
        sqLiteDatabase.execSQL(tableCreation);

        tableCreation = "CREATE TABLE LIST" +
                "(" +
                "id  VARCHAR(8)," +
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
                c = sqLiteDatabase.rawQuery("SELECT * FROM MOVIE mo, BOOK b, SERIE s, MUSIC mu WHERE mo.title = ? OR b.title = ? OR s.title = ? OR mu.title = ?", new String[]{title,title,title,title});
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
        Cursor c = sqLiteDatabase.rawQuery("SELECT * " +
                                                "FROM MOVIE mo, BOOK b, SERIE s, MUSIC mu " +
                                                "WHERE mo.id IN " + listOfIds + " OR b.id IN " + listOfIds + "  OR s.id IN " + listOfIds + "  OR mu.id IN " + listOfIds ,null);
        while(c.moveToNext())
            if(c.getColumnIndex("channel") != -1) //Serie
                media.add(new Serie());
            else if(c.getColumnIndex("album") != -1) //Music
                media.add(new Music());
            else if(c.getColumnIndex("director") != -1) //Movie
                media.add(new Movie());
            else if(c.getColumnIndex("category") != -1) //Book
                media.add(new Book());
        if(c != null)
            c.close();
        return media;
    }

    public List<String> selectListsNames(SQLiteDatabase sqLiteDatabase)
    {
        List<String> nameList = new ArrayList<>();
        Cursor c = sqLiteDatabase.query("Lists", new String[] {"title"},null,null,null,null,null);
        while(c.moveToNext())
             nameList.add(c.getString(c.getColumnIndex("title")));
        if(c != null)
            c.close();
        return nameList;
    }

    private List<String> getIds(SQLiteDatabase sqLiteDatabase, String titleOfTheList)
    {
        List<String> listOfIds = new ArrayList<>();
        Cursor c = sqLiteDatabase.rawQuery("SELECT media " +
                                                "FROM List " +
                                                "WHERE title = ?",new String[] {titleOfTheList});
        while(c.moveToNext())
            listOfIds.add(c.getString(c.getColumnIndex("media")));
        if(c != null)
                c.close();
        return listOfIds;
    }
}
