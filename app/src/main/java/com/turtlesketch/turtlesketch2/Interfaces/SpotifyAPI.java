package com.turtlesketch.turtlesketch2.Interfaces;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

/**
 * Interface that contains all the details to get full coverage of the methods.
 */
public interface SpotifyAPI
{
    /**
     * Authorized the user that log in to access their personal collection from https://accounts.spotify.com.
     * @return Authorized user.
     */
    @GET("authorize?&response_type=code&scope=user-read-private%20user-read-email&state=34fFs29kd09&client_id=9ddbd7d70e1f44cbb80b6c40f40d91ab&redirect_uri=https://patata")
    Call<String> authorizeUser();

    /**
     * Recovers all the playlist from the authorized user to import the content into the application from https://api.spotify.com.
     * @param userId ID of the users to get the playlists.
     * @param auth Authorization token.
     * @return Playlist names and all information about that.
     */
    //Authorization: Bearer {your access token}
    @GET("v1/users/{user_id}/playlists")
    Call<String> getPlaylist(@Path("user_id") String userId, @Header("Authorization") String auth);
}
