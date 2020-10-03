package com.example.turtlescheme;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
                "publishDate DATE," +
                "gender VARCHAR(50)" +
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
                "publishDate DATE," +
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
                "country VARCHAR(20)," +
                "director VARCHAR(50)," +
                "actors VARCHAR(300)," +
                "durationPerEpisode INTEGER NOT NULL," +
                "cover VARCHAR(2083)," +
                "lang VARCHAR(30)," +
                "publishDate DATE NOT NULL," +
                "gender VARCHAR(50) NOT NULL" +
                ");";
        sqLiteDatabase.execSQL(tableCreation);
        tableCreation = "CREATE TABLE MOVIE" +
                "(" +
                "id  VARCHAR(8) PRIMARY KEY," +
                "title VARCHAR(50) NOT NULL," +
                "director VARCHAR(50)," +
                "actors VARCHAR(200)," +
                "publisher VARCHAR(50)," +
                "duration INTEGER NOT NULL," +
                "cover VARCHAR(2083)," +
                "lang VARCHAR(30)," +
                "publishDate DATE NOT NULL," +
                "gender VARCHAR(50) NOT NULL" +
                ");";
        sqLiteDatabase.execSQL(tableCreation);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {

    }
}
