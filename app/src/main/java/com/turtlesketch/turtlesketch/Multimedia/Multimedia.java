package com.turtlesketch.turtlesketch.Multimedia;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Multimedia implements Serializable
{
    public static final String MOVIE  = "MOVIE";
    public static final String MUSIC = "MUSIC";
    public static final String BOOK = "BOOKS";
    public static final String SERIE  = "SERIE";
    public static final String SERIEOMBD = "series";
    public static final String MOVIEOMBD = "movie";

    private String id;
    private String type;
    private String title;
    private List<String> actors_authors;
    private String publishDate;
    private List<String> gender;
    private String language;
    private String cover;
    private String url;

    public Multimedia()
    {

    }

    public Multimedia(String id, String type, String title, String actors_authors, String publishDate, String gender, String language, String cover, String url)
    {
        this.id = id;
        this.type = type;
        this.title = title;
        this.actors_authors = Arrays.asList(actors_authors.split(","));
        this.publishDate = publishDate;
        if(gender.contains(","))
            this.gender = Arrays.asList(gender.split(","));
        else
        {
            this.gender = new ArrayList<>();
            this.gender.add(gender);
        }
        this.language = language;
        this.cover = cover;
        this.url = url;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public List<String> getActors_authors()
    {
        return actors_authors;
    }

    public void setActors_authors(List<String> actors_authors)
    {
        this.actors_authors = actors_authors;
    }

    public String getPublishDate()
    {
        return publishDate;
    }

    public void setPublishDate(String publishDate)
    {
        this.publishDate = publishDate;
    }

    public List<String> getGender()
    {
        return gender;
    }

    public void setGender(List<String> gender)
    {
        this.gender = gender;
    }

    public String getLanguage()
    {
        return language;
    }

    public void setLanguage(String language)
    {
        this.language = language;
    }

    public Bitmap getCoverBitmap()
    {
        try
        {
            URL url = new URL(cover);
            return BitmapFactory.decodeStream(url.openConnection().getInputStream());
        }
        catch(java.io.IOException e)
        {
            return null;
        }
    }

    public String getCover()
    {
        return cover;
    }

    public void setCover(String cover)
    {
        this.cover = cover;
    }

    public String getUrl()
    {
        return  url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public ContentValues getContentValues()
    {
        ContentValues content = new ContentValues();
        content.put("id", id);
        content.put("title", title);
        if(getType().equals(Multimedia.BOOK))
            content.put("author", actors_authors.toString().split("\\[")[1].split("]")[0]);
        else if(getType().equals(Multimedia.MUSIC))
            content.put("artist", actors_authors.toString().split("\\[")[1].split("]")[0]);
        else
            content.put("actors", actors_authors.toString().split("\\[")[1].split("]")[0]);
        if(gender != null)
            content.put("gender", gender.toString().split("\\[")[1].split("]")[0]);
        content.put("publishDate", publishDate);
        content.put("lang", language);
        content.put("cover", cover);
        return content;
    }

    private ByteArrayOutputStream getByteFromImage()
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        getCoverBitmap().compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return  outputStream;
    }

    @NotNull
    @Override
    public String toString()
    {
        if(actors_authors.size() > 0)
            return title + "\n" + actors_authors.toString().split("\\[")[1].split("]")[0];
        else
            return title;
    }
}
