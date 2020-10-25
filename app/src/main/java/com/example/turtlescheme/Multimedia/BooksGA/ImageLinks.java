package com.example.turtlescheme.Multimedia.BooksGA;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ImageLinks implements Serializable
{

   @SerializedName("smallThumbnail")
   @Expose
   private String smallThumbnail;
   @SerializedName("thumbnail")
   @Expose
   private String thumbnail;

   /**
    *
    * @return
    * The smallThumbnail
    */
   public String getSmallThumbnail() {
       return smallThumbnail;
   }

   /**
    *
    * @param smallThumbnail
    * The smallThumbnail
    */
   public void setSmallThumbnail(String smallThumbnail) {
       this.smallThumbnail = smallThumbnail;
   }

   /**
    *
    * @return
    * The thumbnail
    */
   public String getThumbnail() {
       return thumbnail;
   }

   /**
    *
    * @param thumbnail
    * The thumbnail
    */
   public void setThumbnail(String thumbnail) {
       this.thumbnail = thumbnail;
   }

}
