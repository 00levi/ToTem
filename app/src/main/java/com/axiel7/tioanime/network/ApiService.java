package com.axiel7.tioanime.network;

import com.axiel7.tioanime.model.Anime;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("base.json")
    Call<List<Anime>> getAnimes();
}
