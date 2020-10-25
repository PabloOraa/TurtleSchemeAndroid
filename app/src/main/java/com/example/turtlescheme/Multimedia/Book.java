package com.example.turtlescheme.Multimedia;

import android.content.ContentValues;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.List;

public class Book extends Multimedia implements Serializable
{
    private String plot;
    private String publisher;

    public String getPlot()
    {
        return plot;
    }

    public void setPlot(String plot)
    {
        this.plot = plot;
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
        content.remove("gender");
        content.put("category", getGender().toString());
        content.put("publisher", publisher);
        return content;
    }

    @NonNull
    @Override
    public String toString()
    {
        return "ID: " + getId() + "\nTitle: " + getTitle() + "\nAuthor(s): " + getActors_authors();
    }
}
