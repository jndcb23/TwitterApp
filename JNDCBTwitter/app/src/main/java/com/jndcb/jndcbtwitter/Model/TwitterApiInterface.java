package com.jndcb.jndcbtwitter.Model;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface TwitterApiInterface {

    String BASE_URL = "https://api.twitter.com/";

    @FormUrlEncoded
    @POST("oauth2/token")
    Call<OAuth2Token> postCredentials(@Field("grant_type") String grantType);

    @GET("/1.1/statuses/user_timeline.json")
    Call<List<Tweet>> getUserTimeline(@Query("screen_name") String... name);
}
