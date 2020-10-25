package com.example.turtlescheme.Multimedia.BooksGA;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SearchInfo implements Serializable
{
    @SerializedName("textSnippet")
    @Expose
    private String textSnippet;

    /**
     *
     * @return
     * The textSnippet
     */
    public String getTextSnippet() {
        return textSnippet;
    }

    /**
     *
     * @param textSnippet
     * The textSnippet
     */
    public void setTextSnippet(String textSnippet) {
        this.textSnippet = textSnippet;
    }

}
