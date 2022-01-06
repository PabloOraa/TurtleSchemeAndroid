package com.turtlesketch.turtlesketch2.Multimedia.BooksGA;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Epub implements Serializable
{

    @SerializedName("isAvailable")
    @Expose
    private Boolean isAvailable;

    /**
     *
     * @return
     * The isAvailable
     */
    public Boolean getIsAvailable() {
        return isAvailable;
    }

    /**
     *
     * @param isAvailable
     * The isAvailable
     */
    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

}
