package com.example.turtlescheme.Multimedia;

import android.content.ContentValues;

public class Music extends  Multimedia
{
    private String duration;
    private String album;
    private String publisher;

    public String getDuration()
    {
        return duration;
    }

    public void setDuration(String duration)
    {
        this.duration = duration;
    }

    public String getAlbum()
    {
        return album;
    }

    public void setAlbum(String album)
    {
        this.album = album;
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
        content.put("album", album);
        content.put("publisher", publisher);
        return content;
    }
}
