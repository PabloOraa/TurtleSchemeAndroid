package com.turtlesketch.turtlesketch2.Multimedia;

import android.content.ContentValues;

import org.jetbrains.annotations.NotNull;

public class Movie extends Multimedia
{
    private String plot;
    private String director;
    private String duration;

    public Movie()
    {

    }

    public Movie(String id, String title, String actors_authors, String publishDate, String gender, String language, String cover, String url, String plot, String director, String duration)
    {
        super(id,Multimedia.MOVIE,title,actors_authors,publishDate,gender,language,cover,url);
        this.plot = plot;
        this.director = director;
        this.duration = duration;
    }

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

    public String getDuration()
    {
        return duration;
    }

    public void setDuration(String duration)
    {
        this.duration = duration;
    }

    @Override
    public ContentValues getContentValues()
    {
        ContentValues content = super.getContentValues();
        content.put("plot", plot);
        content.put("director", director);
        content.put("duration", duration);
        return content;
    }

    @NotNull
    @Override
    public String toString()
    {
        return super.toString();
    }
}
