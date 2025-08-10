package com.axiel7.tioanime.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.axiel7.tioanime.R;
import com.axiel7.tioanime.model.Anime;
import com.axiel7.tioanime.view.EpisodesActivity; // Asegurate que esta sea la ruta correcta
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class AnimeAdapter extends RecyclerView.Adapter<AnimeAdapter.ViewHolder> {

    private final List<Anime> animeList;
    private final Context context;

    public AnimeAdapter(List<Anime> animeList, Context context) {
        this.animeList = animeList;
        this.context = context;
    }

    @NonNull
    @Override
    public AnimeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_anime, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnimeAdapter.ViewHolder holder, int position) {
        Anime anime = animeList.get(position);
        holder.title.setText(anime.getTitle());
        Glide.with(context).load(anime.getImage()).into(holder.imageView);

        // Abrir EpisodesActivity al hacer clic en un anime
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, EpisodesActivity.class);
            intent.putExtra("animeId", anime.getId());
            intent.putExtra("animeTitle", anime.getTitle());
            intent.putExtra("animeImage", anime.getImage());
            intent.putExtra("animeDescription", anime.getDescription());

// Pasar episodios como serializable
            intent.putExtra("episodes", new ArrayList<>(anime.getEpisodes()));

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return animeList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            imageView = view.findViewById(R.id.image);
        }
    }
}
