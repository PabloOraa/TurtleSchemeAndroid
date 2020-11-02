package com.example.turtlescheme.Multimedia;

import android.content.ContentValues;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Book extends Multimedia implements Serializable
{
    private String plot;
    private String publisher;

    public Book()
    {

    }

    public Book(String id, String title, String actors_authors, String publishDate, String gender, String language, String cover, String url, String plot, String publisher)
    {
        super(id,Multimedia.BOOK,title,actors_authors,publishDate,gender,language,cover,url);
        this.plot = plot;
        this.publisher = publisher;
    }

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
        content.put("category", getGender().toString().split("\\[")[1].split("]")[0]);
        content.put("publisher", publisher);
        return content;
    }

    @NonNull
    @Override
    public String toString()
    {
        return super.toString();
        //return "ID: " + getId() + "\nTitle: " + getTitle() + "\nAuthor(s): " + getActors_authors();
    }
}
