package com.example.newswebviewresponsive;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GetDataService {

@GET("everything")
    Call<News> getAllArticles(@Query("q") String query,@Query("apiKey") String apiKey);

}
