package com.turtlesketch.turtlesketch2.Multimedia;

import android.content.ContentValues;

import org.jetbrains.annotations.NotNull;

public class Serie extends Multimedia
{
    private String plot;
    private String director;
    private String country;
    private String durationPerEpisode;
    private String seasonNumber;

    public Serie()
    {

    }

    public Serie(String id, String title, String actors_authors, String publishDate, String gender, String language, String cover, String url, String plot, String director, String country, String durationPerEpisode, String seasonNumber)
    {
        super(id,Multimedia.SERIE,title,actors_authors,publishDate,gender,language,cover,url);
        this.plot = plot;
        this.director = director;
        this.country = country;
        this.durationPerEpisode = durationPerEpisode;
        this.seasonNumber = seasonNumber;
    }

    public String getPlot()
    {
        return plot;
    }

    public void setPlot(String plot)
    {
        this.plot = plot;
    }

    public String getSeasonNumber()
    {
        return seasonNumber;
    }

    public void setSeasonNumber(String seasonNumber)
    {
        this.seasonNumber = seasonNumber;
    }

    public String getCountry()
    {
        return country;
    }

    public void setCountry(String country)
    {
        this.country = country;
    }

    public String getDurationPerEpisode()
    {
        return durationPerEpisode;
    }

    public void setDurationPerEpisode(String durationPerEpisode)
    {
        this.durationPerEpisode = durationPerEpisode;
    }

    public String getDirector()
    {
        return director;
    }

    public void setDirector(String director)
    {
        this.director = director;
    }

    @Override
    public ContentValues getContentValues()
    {
        ContentValues content = super.getContentValues();
        content.put("plot", plot);
        content.put("director", director);
        content.put("seasonNumber", seasonNumber);
        content.put("country", country);
        content.put("durationPerEpisode", durationPerEpisode);
        return content;
    }

    @NotNull
    @Override
    public String toString()
    {
        return super.toString();
    }
}
