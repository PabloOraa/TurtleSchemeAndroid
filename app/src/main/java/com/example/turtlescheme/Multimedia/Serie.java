package com.example.turtlescheme.Multimedia;

import android.content.ContentValues;

public class Serie extends Multimedia
{
    private String plot;
    private String channel;
    private String director;
    private String country;
    private String durationPerEpisode;
    private int seasonNumber;
    private int chapterNumber;

    public String getPlot()
    {
        return plot;
    }

    public void setPlot(String plot)
    {
        this.plot = plot;
    }

    public int getSeasonNumber()
    {
        return seasonNumber;
    }

    public void setSeasonNumber(int seasonNumber)
    {
        this.seasonNumber = seasonNumber;
    }

    public int getChapterNumber()
    {
        return chapterNumber;
    }

    public void setChapterNumber(int chapterNumber)
    {
        this.chapterNumber = chapterNumber;
    }

    public String getCountry()
    {
        return country;
    }

    public void setCountry(String country)
    {
        this.country = country;
    }

    public String getChannel()
    {
        return channel;
    }

    public void setChannel(String channel)
    {
        this.channel = channel;
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
        content.put("channel", channel);
        content.put("director", director);
        content.put("seasonNumber", seasonNumber);
        content.put("chapterNumber", chapterNumber);
        content.put("country", country);
        content.put("durationPerEpisode", durationPerEpisode);
        return content;
    }
}
