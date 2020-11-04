package com.example.turtlescheme.Multimedia;

import android.content.ContentValues;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class Music extends  Multimedia implements Serializable
{
    private String duration;
    private String publisher;
    private String description;

    public Music()
    {

    }

    public Music(String id, String title, String actors_authors, String publishDate, String gender, String language, String cover, String url, String duration, String publisher, String description)
    {
        super(id,Multimedia.MUSIC,title,actors_authors,publishDate,gender,language,cover,url);
        this.duration = duration;
        this.publisher = publisher;
        this.description = description;
    }

    public String getDuration()
    {
        return duration;
    }

    public void setDuration(String duration)
    {
        this.duration = duration;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
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
        content.put("description", description);
        return content;
    }

    @NotNull
    @Override
    public String toString()
    {
        return super.toString();
    }
}
