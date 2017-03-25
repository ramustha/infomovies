package com.ramusthastudio.infomovies.model;

import com.google.gson.annotations.SerializedName;

public class Seasons {
  @SerializedName("air_date")
  private String airDate;
  @SerializedName("episode_count")
  private int episodeCount;
  @SerializedName("id")
  private int id;
  @SerializedName("poster_path")
  private String posterPath;
  @SerializedName("season_number")
  private int seasonNumber;

  public String getAirDate() { return airDate; }
  public int getEpisodeCount() { return episodeCount; }
  public int getId() { return id; }
  public String getPosterPath() { return posterPath; }
  public int getSeasonNumber() { return seasonNumber; }

  public Seasons setAirDate(String aAirDate) {
    airDate = aAirDate;
    return this;
  }
  public Seasons setEpisodeCount(int aEpisodeCount) {
    episodeCount = aEpisodeCount;
    return this;
  }
  public Seasons setId(int aId) {
    id = aId;
    return this;
  }
  public Seasons setPosterPath(String aPosterPath) {
    posterPath = aPosterPath;
    return this;
  }
  public Seasons setSeasonNumber(int aSeasonNumber) {
    seasonNumber = aSeasonNumber;
    return this;
  }

  @Override public String toString() {
    return "Seasons{" +
        "airDate='" + airDate + '\'' +
        ", episodeCount='" + episodeCount + '\'' +
        ", id='" + id + '\'' +
        ", posterPath='" + posterPath + '\'' +
        ", seasonNumber='" + seasonNumber + '\'' +
        '}';
  }
}
