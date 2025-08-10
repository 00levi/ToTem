package com.axiel7.tioanime.model;

import java.io.Serializable;

public class Episode implements Serializable {
    private int episodeNumber;
    private String title;
    private String url;

    public int getEpisodeNumber() { return episodeNumber; }
    public void setEpisodeNumber(int episodeNumber) { this.episodeNumber = episodeNumber; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
}
