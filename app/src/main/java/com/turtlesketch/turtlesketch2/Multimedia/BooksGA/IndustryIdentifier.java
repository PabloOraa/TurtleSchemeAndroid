package com.turtlesketch.turtlesketch2.Multimedia.BooksGA;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class IndustryIdentifier implements Serializable
{

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("identifier")
    @Expose
    private String identifier;

    /**
     *
     * @return
     * The type
     */
    public String getType() {
        return type;
    }

    /**
     *
     * @param type
     * The type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     *
     * @return
     * The identifier
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     *
     * @param identifier
     * The identifier
     */
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

}
