package com.axiel7.tioanime.view;

import android.os.Bundle;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.axiel7.tioanime.R;
import com.axiel7.tioanime.adapter.AnimeAdapter;
import com.axiel7.tioanime.model.Anime;
import com.axiel7.tioanime.network.ApiService;
import com.axiel7.tioanime.network.RetrofitClient;

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

        // Importante para TV: permitir que los hijos reciban el foco primero
        recyclerView.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        recyclerView.setFocusable(true);
        recyclerView.setFocusableInTouchMode(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<List<Anime>> call = apiService.getAnimes();

        call.enqueue(new Callback<List<Anime>>() {
            @Override
            public void onResponse(Call<List<Anime>> call, Response<List<Anime>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Anime> animeList = response.body();
                    animeAdapter = new AnimeAdapter(animeList, MainActivity.this);
                    recyclerView.setAdapter(animeAdapter);

                    // Pedir foco al primer item una vez que el RecyclerView tenga vistas
                    recyclerView.post(() -> {
                        if (recyclerView.getChildCount() > 0) {
                            recyclerView.getChildAt(0).requestFocus();
                        } else {
                            recyclerView.requestFocus();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Anime>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reintentar pedir foco al volver a la actividad
        if (recyclerView != null) {
            recyclerView.post(() -> {
                if (recyclerView.getChildCount() > 0) {
                    recyclerView.getChildAt(0).requestFocus();
                } else {
                    recyclerView.requestFocus();
                }
            });
        }
    }
}
