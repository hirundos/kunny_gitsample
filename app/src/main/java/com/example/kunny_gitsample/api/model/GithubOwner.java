package com.example.kunny_gitsample.api.model;

import com.google.gson.annotations.SerializedName;

public final class GithubOwner {
    public final String login;

    @SerializedName("avatar_url")
    public final String avatarUrl;


    public GithubOwner(String login, String avatarUrl) {
        this.avatarUrl = avatarUrl;
        this.login = login;
    }
}
