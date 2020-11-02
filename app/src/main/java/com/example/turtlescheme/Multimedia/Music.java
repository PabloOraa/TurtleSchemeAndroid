package com.example.turtlescheme.Multimedia;

import android.content.ContentValues;

import java.io.Serializable;

public class Music extends  Multimedia implements Serializable
{
    private String duration;
    private String publisher;

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
}
