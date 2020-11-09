package com.turtlesketch.turtlesketch.Multimedia.MYSQL.SerieGA;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result
{
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("director")
    @Expose
    private String director;
    @SerializedName("actors")
    @Expose
    private String actors;
    @SerializedName("plot")
    @Expose
    private String plot;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("cover")
    @Expose
    private String cover;
    @SerializedName("lang")
    @Expose
    private String lang;
    @SerializedName("publishDate")
    @Expose
    private String publishDate;
    @SerializedName("durationPerEpisode")
    @Expose
    private String durationPerEpisode;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("seasonNumber")
    @Expose
    private String seasonNumber;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getDurationPerEpisode() {
        return durationPerEpisode;
    }

    public void setDurationPerEpisode(String durationPerEpisode) {
        this.durationPerEpisode = durationPerEpisode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getSeasonNumber() {
        return seasonNumber;
    }

    public void setSeasonNumber(String seasonNumber) {
        this.seasonNumber = seasonNumber;
    }
}