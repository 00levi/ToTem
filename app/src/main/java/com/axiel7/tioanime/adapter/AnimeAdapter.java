package com.axiel7.tioanime.adapter;

import android.content.Context;
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
import com.axiel7.tioanime.model.Anime;
import com.axiel7.tioanime.view.EpisodesActivity;
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

        // Click normal
        holder.itemView.setOnClickListener(v -> openEpisodes(anime));

        // Center/Enter on D-Pad should activate click
        holder.itemView.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN &&
                    (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER)) {
                v.performClick();
                return true;
            }
            return false;
        });

        // Focus visual handled by XML selector; you can add scale here if you want
    }

    private void openEpisodes(Anime anime) {
        Intent intent = new Intent(context, EpisodesActivity.class);
        intent.putExtra("animeId", anime.getId());
        intent.putExtra("animeTitle", anime.getTitle());
        intent.putExtra("animeImage", anime.getImage());
        intent.putExtra("animeDescription", anime.getDescription());
        intent.putExtra("episodes", new ArrayList<>(anime.getEpisodes()));
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return animeList == null ? 0 : animeList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            imageView = view.findViewById(R.id.image);

            // Importante para TV navigation
            view.setFocusable(true);
            view.setFocusableInTouchMode(true);

            // Efecto de foco (opcional: escala)
            view.setOnFocusChangeListener((v, hasFocus) -> {
                if (hasFocus) {
                    v.animate().scaleX(1.05f).scaleY(1.05f).setDuration(120).start();
                    v.setElevation(12f);
                    v.setBackgroundColor(Color.parseColor("#901050")); // Naranja
                } else {
                    v.animate().scaleX(1f).scaleY(1f).setDuration(120).start();
                    v.setElevation(0f);
                    v.setBackgroundColor(Color.TRANSPARENT); // Vuelve al original
                }
            });

        }
    }
}
