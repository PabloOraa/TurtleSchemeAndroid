package com.example.turtlescheme.Multimedia.MusicGA;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MusicGADetail
{

    @SerializedName("album")
    @Expose
    private List<Album> album = null;

    public List<Album> getAlbum() {
        return album;
    }

    public void setAlbum(List<Album> album) {
        this.album = album;
    }

}