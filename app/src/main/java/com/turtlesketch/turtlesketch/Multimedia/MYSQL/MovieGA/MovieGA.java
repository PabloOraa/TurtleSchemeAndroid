package com.turtlesketch.turtlesketch.Multimedia.MYSQL.MovieGA;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MovieGA
{
    @SerializedName("total_results")
    @Expose
    private String totalResults;
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("results")
    @Expose
    private List<Result> results = null;

    public String getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(String totalResults) {
        this.totalResults = totalResults;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }
}