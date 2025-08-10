package com.axiel7.tioanime.view;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.axiel7.tioanime.R;
import com.axiel7.tioanime.adapter.AnimeAdapter;
import com.axiel7.tioanime.model.Anime;
import com.axiel7.tioanime.model.Episode;
import com.axiel7.tioanime.network.ApiService;
import com.axiel7.tioanime.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AnimeAdapter animeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<List<Anime>> call = apiService.getAnimes();

        call.enqueue(new Callback<List<Anime>>() {
            @Override
            public void onResponse(Call<List<Anime>> call, Response<List<Anime>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Anime> animeList = response.body();
                    for (Anime anime : animeList) {
                        System.out.println("Anime: " + anime.getTitle() + " - Episodios: " + anime.getEpisodes().size());
                    }

                    animeAdapter = new AnimeAdapter(animeList, MainActivity.this);
                    recyclerView.setAdapter(animeAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Anime>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

}
