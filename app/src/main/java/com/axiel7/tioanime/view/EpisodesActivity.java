    package com.axiel7.tioanime.view;

    import android.content.Intent;
    import android.os.Bundle;
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


            Intent intent = getIntent();
            String title = intent.getStringExtra("animeTitle");
            String imageUrl = intent.getStringExtra("animeImage");
            String description = intent.getStringExtra("animeDescription");
            animeDescription.setText(description);
            episodes = (ArrayList<Episode>) intent.getSerializableExtra("episodes");

            animeTitle.setText(title);
            Glide.with(this).load(imageUrl).into(animeImage);

            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            episodeAdapter = new EpisodeAdapter(episodes, imageUrl);
            recyclerView.setAdapter(episodeAdapter);
        }
    }
