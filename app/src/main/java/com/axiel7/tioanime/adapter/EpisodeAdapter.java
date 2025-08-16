package com.axiel7.tioanime.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.axiel7.tioanime.R;
import com.axiel7.tioanime.model.Episode;
import com.axiel7.tioanime.view.VideoPlayerActivity;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class EpisodeAdapter extends RecyclerView.Adapter<EpisodeAdapter.ViewHolder> {

    private final List<Episode> allEpisodes;     // lista completa
    private final List<Episode> visibleEpisodes; // lista visible (paginada)
    private final String imageUrl;
    private static final int PAGE_SIZE = 50;
    private int currentPage = 0;
    private boolean isLoading = false;

    public EpisodeAdapter(List<Episode> episodes, String imageUrl) {
        this.allEpisodes = episodes != null ? episodes : new ArrayList<>();
        this.visibleEpisodes = new ArrayList<>();
        this.imageUrl = imageUrl;
        // Carga inicial
        loadMore();
    }

    public boolean isLoading() {
        return isLoading;
    }

    /** Carga la siguiente página de episodios de forma segura */
    public void loadMore() {
        if (isLoading) return;
        isLoading = true;

        int start = currentPage * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, allEpisodes.size());
        if (start < end) {
            int prevSize = visibleEpisodes.size();
            visibleEpisodes.addAll(allEpisodes.subList(start, end));
            notifyItemRangeInserted(prevSize, end - start);
            currentPage++;
        }

        isLoading = false;
    }

    @NonNull
    @Override
    public EpisodeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_episode, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EpisodeAdapter.ViewHolder holder, int position) {
        Episode episode = visibleEpisodes.get(position);

        // Título y "ID"
        String title = episode.getTitle() != null && !episode.getTitle().isEmpty()
                ? episode.getTitle()
                : "Episodio " + episode.getEpisodeNumber();

        holder.episodeTitle.setText(title);
        holder.episodeDate.setText("ID: " + episode.getEpisodeNumber());

        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .into(holder.episodeImage);

        holder.itemView.setOnClickListener(v -> {
            // Armamos listas livianas para el reproductor (evita TransactionTooLarge)
            ArrayList<String> urls = new ArrayList<>(allEpisodes.size());
            ArrayList<String> titles = new ArrayList<>(allEpisodes.size());
            for (Episode e : allEpisodes) {
                String t = (e.getTitle() != null && !e.getTitle().isEmpty())
                        ? e.getTitle()
                        : "Episodio " + e.getEpisodeNumber();
                titles.add(t);
                urls.add(e.getUrl());
            }

            // Como vamos cargando desde 0 hacia arriba, el índice global coincide con 'position'
            int globalIndex = position;

            Intent intent = new Intent(v.getContext(), VideoPlayerActivity.class);
            intent.putStringArrayListExtra("episodeUrls", urls);
            intent.putStringArrayListExtra("episodeTitles", titles);
            intent.putExtra("currentIndex", globalIndex);
            v.getContext().startActivity(intent);
        });

        // Soporte para OK/Enter en TV
        holder.itemView.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN &&
                    (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER)) {
                v.performClick();
                return true;
            }
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return visibleEpisodes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView episodeTitle, episodeDate;
        ImageView episodeImage;

        public ViewHolder(View itemView) {
            super(itemView);
            episodeTitle = itemView.findViewById(R.id.episode_number);
            episodeDate  = itemView.findViewById(R.id.date);
            episodeImage = itemView.findViewById(R.id.movie_image);

            itemView.setFocusable(true);
            itemView.setFocusableInTouchMode(true);

            itemView.setOnFocusChangeListener((v, hasFocus) -> {
                if (hasFocus) {
                    v.animate().scaleX(1.03f).scaleY(1.03f).setDuration(120).start();
                    v.setElevation(8f);
                    v.setBackgroundColor(Color.parseColor("#901050"));
                } else {
                    v.animate().scaleX(1f).scaleY(1f).setDuration(120).start();
                    v.setElevation(0f);
                    v.setBackgroundColor(Color.TRANSPARENT);
                }
            });
        }
    }
}
