package com.example.turtlescheme.Multimedia;

import android.content.ContentValues;

public class Movie extends Multimedia
{
    private String plot;
    private String director;
    private String publisher;

    public String getPlot()
    {
        return plot;
    }

    public void setPlot(String plot)
    {
        this.plot = plot;
    }

    public String getDirector()
    {
        return director;
    }

    public void setDirector(String director)
    {
        this.director = director;
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
        content.put("plot", plot);
        content.put("director", director);
        content.put("publisher", publisher);
        return content;
    }
}
