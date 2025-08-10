package com.axiel7.tioanime.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.axiel7.tioanime.R;
import com.axiel7.tioanime.model.Episode;
import com.axiel7.tioanime.view.VideoPlayerActivity; // Asegúrate de importar tu actividad
import com.bumptech.glide.Glide;

import java.util.List;

public class EpisodeAdapter extends RecyclerView.Adapter<EpisodeAdapter.ViewHolder> {

    private final List<Episode> episodes;
    private final String imageUrl;

    public EpisodeAdapter(List<Episode> episodes, String imageUrl) {
        this.episodes = episodes;
        this.imageUrl = imageUrl;
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
        Episode episode = episodes.get(position);
        holder.episodeTitle.setText(episode.getTitle());
        holder.episodeDate.setText("ID: " + episode.getEpisodeNumber());
        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .into(holder.episodeImage);

        // Listener para abrir el video
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), VideoPlayerActivity.class);
            intent.putExtra("videoUrl", episode.getUrl());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return episodes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView episodeTitle, episodeDate;
        ImageView episodeImage;

        public ViewHolder(View itemView) {
            super(itemView);
            episodeTitle = itemView.findViewById(R.id.episode_number);
            episodeDate = itemView.findViewById(R.id.date);
            episodeImage = itemView.findViewById(R.id.movie_image);
        }
    }
}
