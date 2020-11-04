package com.example.turtlescheme.Multimedia;

import android.content.ContentValues;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class Music extends  Multimedia implements Serializable
{
    private String duration;
    private String publisher;

    public Music()
    {

    }

    public Music(String id, String title, String actors_authors, String publishDate, String gender, String language, String cover, String url, String duration, String publisher)
    {
        super(id,Multimedia.MUSIC,title,actors_authors,publishDate,gender,language,cover,url);
        this.duration = duration;
        this.publisher = publisher;
    }

    public String getDuration()
    {
        return duration;
    }

    public void setDuration(String duration)
    {
        this.duration = duration;
    }

    public String getPublisher()
    {
        return publisher;
    }

    public void setPublisher(String publisher)
    {
        this.publisher = publisher;
    }

    @Override
    public ContentValues getContentValues()
    {
        ContentValues content = super.getContentValues();
        content.put("duration", duration);
        content.put("publisher", publisher);
        return content;
    }

    @NotNull
    @Override
    public String toString()
    {
        return super.toString();
    }
}
