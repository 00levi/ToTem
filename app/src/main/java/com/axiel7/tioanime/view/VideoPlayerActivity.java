package com.axiel7.tioanime.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.axiel7.tioanime.R;
import com.axiel7.tioanime.utils.DeviceUtils;

import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;
import org.videolan.libvlc.util.VLCVideoLayout;

import java.util.ArrayList;

public class VideoPlayerActivity extends AppCompatActivity {

    private VLCVideoLayout videoLayout;
    private LibVLC libVLC;
    private MediaPlayer mediaPlayer;
    private LinearLayout controlBar;
    private ImageButton btnPlayPause, btnForward, btnRewind, btnPrevEpisode, btnNextEpisode;

    private boolean isPlaying = true;
    private boolean isTVDevice;
    private final Handler hideHandler = new Handler();
    private static final int HIDE_DELAY = 5000;

    private ArrayList<String> episodeUrls;
    private ArrayList<String> episodeTitles; // opcional, por si querés mostrar algo
    private int currentIndex = 0;

    private final Runnable hideRunnable = () -> controlBar.setVisibility(View.GONE);

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Evitar suspensión / screensaver
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_video_player);

        isTVDevice = DeviceUtils.isTV(this);

        videoLayout      = findViewById(R.id.vlcVideoLayout);
        controlBar       = findViewById(R.id.controlBar);
        btnPlayPause     = findViewById(R.id.btnPlayPause);
        btnForward       = findViewById(R.id.btnForward);
        btnRewind        = findViewById(R.id.btnRewind);
        btnPrevEpisode   = findViewById(R.id.btnPrevEpisode);
        btnNextEpisode   = findViewById(R.id.btnNextEpisode);

        // Recibir listas livianas
        episodeUrls   = getIntent().getStringArrayListExtra("episodeUrls");
        episodeTitles = getIntent().getStringArrayListExtra("episodeTitles");
        currentIndex  = getIntent().getIntExtra("currentIndex", 0);

        if (episodeUrls == null || episodeUrls.isEmpty() || currentIndex < 0 || currentIndex >= episodeUrls.size()) {
            finish();
            return;
        }

        // VLC
        ArrayList<String> options = new ArrayList<>();
        options.add("--network-caching=1500");
        options.add("--no-drop-late-frames");
        options.add("--no-skip-frames");
        libVLC = new LibVLC(this, options);
        mediaPlayer = new MediaPlayer(libVLC);

        mediaPlayer.setEventListener(event -> {
            if (event.type == MediaPlayer.Event.EndReached) {
                runOnUiThread(this::playNextEpisode);
            }
        });

        mediaPlayer.attachViews(videoLayout, null, false, false);
        playEpisode(episodeUrls.get(currentIndex));

        // Botones
        btnPlayPause.setOnClickListener(v -> togglePlayPause());
        btnForward.setOnClickListener(v -> seekBy(10000));
        btnRewind.setOnClickListener(v -> seekBy(-10000));
        btnPrevEpisode.setOnClickListener(v -> playPreviousEpisode());
        btnNextEpisode.setOnClickListener(v -> playNextEpisode());

        // Toque (en móvil) para mostrar/ocultar
        videoLayout.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (controlBar.getVisibility() == View.GONE) controlBar.setVisibility(View.VISIBLE);
                showControls();
            }
            return true;
        });

        showControls();
    }

    private void playEpisode(String url) {
        mediaPlayer.stop();
        Media media = new Media(libVLC, android.net.Uri.parse(url));
        media.setHWDecoderEnabled(true, false);
        media.addOption(":network-caching=1500");
        mediaPlayer.setMedia(media);
        media.release();
        mediaPlayer.play();
        isPlaying = true;
        btnPlayPause.setImageResource(android.R.drawable.ic_media_pause);
    }

    private void playNextEpisode() {
        if (currentIndex + 1 < episodeUrls.size()) {
            currentIndex++;
            playEpisode(episodeUrls.get(currentIndex));
        } else {
            finish();
        }
    }

    private void playPreviousEpisode() {
        if (currentIndex - 1 >= 0) {
            currentIndex--;
            playEpisode(episodeUrls.get(currentIndex));
        } else {
            finish();
        }
    }

    private void togglePlayPause() {
        if (isPlaying) {
            mediaPlayer.pause();
            btnPlayPause.setImageResource(android.R.drawable.ic_media_play);
        } else {
            mediaPlayer.play();
            btnPlayPause.setImageResource(android.R.drawable.ic_media_pause);
        }
        isPlaying = !isPlaying;
        showControls();
    }

    private void seekBy(long millis) {
        long current = mediaPlayer.getTime();
        mediaPlayer.setTime(Math.max(current + millis, 0));
        showControls();
    }

    private void showControls() {
        controlBar.setVisibility(View.VISIBLE);
        hideHandler.removeCallbacks(hideRunnable);
        hideHandler.postDelayed(hideRunnable, HIDE_DELAY);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
            case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                togglePlayPause();
                return true;

            case KeyEvent.KEYCODE_DPAD_LEFT:
            case KeyEvent.KEYCODE_MEDIA_REWIND:
                seekBy(-10000);
                return true;

            case KeyEvent.KEYCODE_DPAD_RIGHT:
            case KeyEvent.KEYCODE_MEDIA_FAST_FORWARD:
                seekBy(10000);
                return true;

            case KeyEvent.KEYCODE_MEDIA_NEXT:
            case KeyEvent.KEYCODE_BUTTON_R1:
                playNextEpisode();
                return true;

            case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
            case KeyEvent.KEYCODE_BUTTON_L1:
                playPreviousEpisode();
                return true;

            case KeyEvent.KEYCODE_BACK:
                finish();
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Volver a permitir reposo
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.detachViews();
            mediaPlayer.release();
        }
        if (libVLC != null) {
            libVLC.release();
        }
    }
}
