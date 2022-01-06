package com.turtlesketch.turtlesketch2.Interfaces;

import com.turtlesketch.turtlesketch2.Multimedia.MusicGA.MusicGA;
import com.turtlesketch.turtlesketch2.Multimedia.MusicGA.MusicGADetail;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Interface that contains all the details to get full coverage of the methods.
 */
public interface MusicAPI
{
    /**
     * Layout to get  all the music from the query on Deezer. It is currently not used.
     * @param text Text to search.
     * @return MusicGA with all the information.
     * @deprecated
     */
    @GET("search?")
    Call<MusicGA> getMusic(@Query("q") String text);

    /**
     * Get the artist from the query on https://www.theaudiodb.com/.
     * @param artist Artist to get all the information.
     * @return MusicGA with all the information from the query
     */
    @GET("api/v1/json/1/search.php")
    Call<MusicGA> getArtist(@Query("s") String artist);

    /**
     * Get the music from the query on https://www.theaudiodb.com/.
     * @param idArtist id of the artist to get all the information from that artist.
     * @return MusicGADetail with all the information from the query
     */
    @GET("api/v1/json/1/album.php")
    Call<MusicGADetail> getAlbums(@Query("i") String idArtist);
}
