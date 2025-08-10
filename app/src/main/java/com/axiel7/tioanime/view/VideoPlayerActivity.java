package com.axiel7.tioanime.view;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.axiel7.tioanime.R;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.PlayerView;

public class VideoPlayerActivity extends AppCompatActivity {

    private ExoPlayer player;
    private PlayerView playerView;
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        playerView = findViewById(R.id.player_view);
        backButton = findViewById(R.id.back_button);

        String videoUrl = getIntent().getStringExtra("videoUrl");

        if (videoUrl != null && !videoUrl.isEmpty()) {
            Uri videoUri = Uri.parse(videoUrl);

            player = new ExoPlayer.Builder(this).build();
            playerView.setPlayer(player);

            MediaItem mediaItem = MediaItem.fromUri(videoUri);
            player.setMediaItem(mediaItem);
            player.prepare();
            player.setPlayWhenReady(true);
        }

        backButton.setOnClickListener(v -> {
            if (player != null) {
                player.release();
            }
            finish();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.release();
        }
    }
}
