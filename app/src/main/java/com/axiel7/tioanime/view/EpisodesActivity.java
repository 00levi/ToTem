package com.axiel7.tioanime.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.axiel7.tioanime.R;
import com.axiel7.tioanime.adapter.EpisodeAdapter;
import com.axiel7.tioanime.model.Episode;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class EpisodesActivity extends AppCompatActivity {

    private TextView animeTitle;
    private ImageView animeImage;
    private RecyclerView recyclerView;
    private EpisodeAdapter episodeAdapter;
    private ArrayList<Episode> episodes;
    private TextView animeDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episodes);

        animeTitle = findViewById(R.id.animeTitle);
        animeImage = findViewById(R.id.animeImage);
        recyclerView = findViewById(R.id.recyclerView);
        animeDescription = findViewById(R.id.animeDescription);

        recyclerView.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        recyclerView.setFocusable(true);
        recyclerView.setFocusableInTouchMode(true);

        Intent intent = getIntent();
        String title = intent.getStringExtra("animeTitle");
        String imageUrl = intent.getStringExtra("animeImage");
        String description = intent.getStringExtra("animeDescription");
        animeDescription.setText(description);

        Object rawEpisodes = intent.getSerializableExtra("episodes");
        if (rawEpisodes instanceof ArrayList) {
            episodes = (ArrayList<Episode>) rawEpisodes;
        } else {
            episodes = new ArrayList<>();
        }

        animeTitle.setText(title);
        Glide.with(this).load(imageUrl).into(animeImage);

        LinearLayoutManager lm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(lm);

        episodeAdapter = new EpisodeAdapter(episodes, imageUrl);
        recyclerView.setAdapter(episodeAdapter);

        // Carga incremental segura al acercarse al final
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView rv, int dx, int dy) {
                super.onScrolled(rv, dx, dy);
                int total = episodeAdapter.getItemCount();
                int last = lm.findLastVisibleItemPosition();
                if (!episodeAdapter.isLoading() && last >= total - 5) {
                    episodeAdapter.loadMore();
                }
            }
        });

        // Pedir foco inicial
        recyclerView.post(() -> {
            if (recyclerView.getChildCount() > 0) {
                recyclerView.getChildAt(0).requestFocus();
            } else {
                recyclerView.requestFocus();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        recyclerView.post(() -> {
            if (recyclerView.getChildCount() > 0) {
                recyclerView.getChildAt(0).requestFocus();
            } else {
                recyclerView.requestFocus();
            }
        });
    }
}
