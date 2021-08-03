package com.example.kunny_gitsample.api.model;


import com.google.gson.annotations.SerializedName;

public final class GithubAccessToken {

    @SerializedName("access_token")
    public final String accessToken;

    public final String scope;

    @SerializedName("token_type")
    public final String tokenType;

    public GithubAccessToken(String accessToken, String scope, String tokenType) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.scope = scope;
    }
}
