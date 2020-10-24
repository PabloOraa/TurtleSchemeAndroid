package com.example.turtlescheme.Multimedia;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.List;

public abstract class Multimedia
{
    public static final String MOVIE  = "MOVIE";
    public static final String MUSIC = "MUSIC";
    public static final String BOOK = "BOOK";
    public static final String SERIE  = "SERIE";

    private String id;
    private String type;
    private String title;
    private List<String> actors_authors;
    private Date publishDate;
    private String gender;
    private String language;
    private Image cover;

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

    public Date getPublishDate()
    {
        return publishDate;
    }

    public void setPublishDate(Date publishDate)
    {
        this.publishDate = publishDate;
    }

    public String getGender()
    {
        return gender;
    }

    public void setGender(String gender)
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

    public Image getCover()
    {
        return cover;
    }

    public void setCover(Image cover)
    {
        this.cover = cover;
    }

    public ContentValues getContentValues()
    {
        ContentValues content = new ContentValues();
        content.put("title", title);
        if(getType().equals(Multimedia.BOOK))
            content.put("author", actors_authors.toString());
        else if(getType().equals(Multimedia.MUSIC))
            content.put("artist", actors_authors.toString());
        else
            content.put("actors", actors_authors.toString());
        content.put("gender", gender);
        content.put("publishDate", String.valueOf(new java.sql.Date(publishDate.getTime())));
        content.put("lang", language);
        ByteArrayOutputStream outputStream = getByteFromImage();
        content.put("cover", outputStream.toByteArray());
        return content;
    }

    private ByteArrayOutputStream getByteFromImage()
    {
        ByteBuffer buffer = cover.getPlanes()[0].getBuffer();
        byte[] bytes = new byte[buffer.capacity()];
        buffer.get(bytes);
        Bitmap bitmapImage = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, null);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmapImage.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return  outputStream;
    }
}
