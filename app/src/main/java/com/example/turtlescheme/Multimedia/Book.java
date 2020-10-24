package com.example.turtlescheme.Multimedia;

import android.content.ContentValues;

public class Book extends Multimedia
{
    private String plot;
    private String category;
    private String publisher;

    public String getPlot()
    {
        return plot;
    }

    public void setPlot(String plot)
    {
        this.plot = plot;
    }

    public String getCategory()
    {
        return category;
    }

    public void setCategory(String category)
    {
        this.category = category;
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
        content.put("category", category);
        content.put("publisher", publisher);
        return content;
    }
}
