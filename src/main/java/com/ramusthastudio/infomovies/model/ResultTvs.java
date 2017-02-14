
package com.ramusthastudio.infomovies.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ResultTvs {
  @SerializedName("poster_path")
  private String posterPath;
  @SerializedName("popularity")
  private double popularity;
  @SerializedName("id")
  private int id;
  @SerializedName("backdrop_path")
  private String backdropPath;
  @SerializedName("vote_average")
  private double voteAverage;
  @SerializedName("overview")
  private String overview;
  @SerializedName("first_air_date")
  private String firstAirDate;
  @SerializedName("origin_country")
  private final List<String> originCountry = null;
  @SerializedName("genre_ids")
  private final List<Integer> genreIds = null;
  @SerializedName("original_language")
  private String originalLanguage;
  @SerializedName("vote_count")
  private int voteCount;
  @SerializedName("name")
  private String name;
  @SerializedName("original_name")
  private String originalName;

  public String getPosterPath() { return posterPath; }
  public double getPopularity() { return popularity; }
  public int getId() { return id; }
  public String getBackdropPath() { return backdropPath; }
  public double getVoteAverage() { return voteAverage; }
  public String getOverview() { return overview; }
  public String getFirstAirDate() { return firstAirDate; }
  public List<String> getOriginCountry() { return originCountry; }
  public List<Integer> getGenreIds() { return genreIds; }
  public String getOriginalLanguage() { return originalLanguage; }
  public int getVoteCount() { return voteCount; }
  public String getName() { return name; }
  public String getOriginalName() { return originalName; }

  @Override public String toString() {
    return "ResultTvs{" +
        "posterPath='" + posterPath + '\'' +
        ", popularity=" + popularity +
        ", id=" + id +
        ", backdropPath='" + backdropPath + '\'' +
        ", voteAverage=" + voteAverage +
        ", overview='" + overview + '\'' +
        ", firstAirDate='" + firstAirDate + '\'' +
        ", originCountry=" + originCountry +
        ", genreIds=" + genreIds +
        ", originalLanguage='" + originalLanguage + '\'' +
        ", voteCount=" + voteCount +
        ", name='" + name + '\'' +
        ", originalName='" + originalName + '\'' +
        '}';
  }
}
