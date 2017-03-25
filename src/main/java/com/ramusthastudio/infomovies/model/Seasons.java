package com.ramusthastudio.infomovies.model;

import com.google.gson.annotations.SerializedName;

public class Seasons {
  @SerializedName("air_date")
  private String airDate;
  @SerializedName("episode_count")
  private String episodeCount;
  @SerializedName("id")
  private String id;
  @SerializedName("poster_path")
  private String posterPath;
  @SerializedName("season_number")
  private String seasonNumber;

  public String getAirDate() { return airDate; }
  public String getEpisodeCount() { return episodeCount; }
  public String getId() { return id; }
  public String getPosterPath() { return posterPath; }
  public String getSeasonNumber() { return seasonNumber; }

  public Seasons setAirDate(String aAirDate) {
    airDate = aAirDate;
    return this;
  }
  public Seasons setEpisodeCount(String aEpisodeCount) {
    episodeCount = aEpisodeCount;
    return this;
  }
  public Seasons setId(String aId) {
    id = aId;
    return this;
  }
  public Seasons setPosterPath(String aPosterPath) {
    posterPath = aPosterPath;
    return this;
  }
  public Seasons setSeasonNumber(String aSeasonNumber) {
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
