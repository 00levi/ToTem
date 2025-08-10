package com.axiel7.tioanime.model;

import java.io.Serializable;
import java.util.List;

public class Anime implements Serializable {
    private int id;
    private String animeId;
    private String title;
    private String image;
    private String description;
    private String type;
    private List<Episode> episodes;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getAnimeId() { return animeId; }
    public void setAnimeId(String animeId) { this.animeId = animeId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public List<Episode> getEpisodes() { return episodes; }
    public void setEpisodes(List<Episode> episodes) { this.episodes = episodes; }
}
