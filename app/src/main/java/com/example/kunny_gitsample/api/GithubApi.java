package com.example.kunny_gitsample.api;

import com.example.kunny_gitsample.api.model.GithubRepo;
import com.example.kunny_gitsample.api.model.RepoSearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GithubApi {

    @GET("search/repository")
    Call<RepoSearchResponse> searchRepository(String query);

    @GET("repos/{owner}/{name}")
    Call<GithubRepo> getRepository(@Path("owner") String ownerLogin, @Path("name") String repoName);
}
