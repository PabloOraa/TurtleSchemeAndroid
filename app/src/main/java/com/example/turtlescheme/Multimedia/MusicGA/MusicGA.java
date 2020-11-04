package com.example.turtlescheme.Multimedia.MusicGA;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/*
public class MusicGA
{
        @SerializedName("data")
        @Expose
        private List<Datum> data = null;

        public List<Datum> getData()
        {
            return data;
        }

        public void setData(List<Datum> data)
        {
            this.data = data;
        }
}*/

public class MusicGA
{

        @SerializedName("artists")
        @Expose
        private List<Artist> artists = null;

        public List<Artist> getArtists()
        {
                return artists;
        }

        public void setArtists(List<Artist> artists)
        {
                this.artists = artists;
        }

}