package com.example.kunny_gitsample.api;

import androidx.annotation.NonNull;

import com.example.kunny_gitsample.api.model.GithubAccessToken;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface AuthApi {

    @FormUrlEncoded
    @POST("login/oauth/access_token")
    @Headers("Accept: application/json")
    Call<GithubAccessToken> getAccessToken(
            @NonNull @Field("client_id") String clientId,
            @NonNull @Field("client_secret") String clientSecret,
            @NonNull @Field("code") String code
    );
}
