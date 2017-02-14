package com.ramusthastudio.infomovies.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ResultMovieDetail {
  @SerializedName("adult")
  private boolean adult;
  @SerializedName("backdrop_path")
  private String backdropPath;
  // @SerializedName("belongs_to_collection")
  // private String belongsToCollection;
  @SerializedName("budget")
  private int budget;
  @SerializedName("genres")
  private final List<Genre> genres = null;
  @SerializedName("homepage")
  private String homepage;
  @SerializedName("id")
  private int id;
  @SerializedName("imdb_id")
  private String imdbId;
  @SerializedName("original_language")
  private String originalLanguage;
  @SerializedName("original_title")
  private String originalTitle;
  @SerializedName("overview")
  private String overview;
  @SerializedName("popularity")
  private double popularity;
  @SerializedName("poster_path")
  private String posterPath;
  @SerializedName("production_companies")
  private final List<ProductionCompany> productionCompanies = null;
  @SerializedName("production_countries")
  private final List<ProductionCountry> productionCountries = null;
  @SerializedName("release_date")
  private String releaseDate;
  @SerializedName("revenue")
  private int revenue;
  @SerializedName("runtime")
  private int runtime;
  @SerializedName("spoken_languages")
  private final List<SpokenLanguage> spokenLanguages = null;
  @SerializedName("status")
  private String status;
  @SerializedName("tagline")
  private String tagline;
  @SerializedName("title")
  private String title;
  @SerializedName("video")
  private boolean video;
  @SerializedName("vote_average")
  private double voteAverage;
  @SerializedName("vote_count")
  private int voteCount;

  public boolean isAdult() { return adult; }
  public String getBackdropPath() { return backdropPath; }
  // public String getBelongsToCollection() { return belongsToCollection; }
  public int getBudget() { return budget; }
  public List<Genre> getGenres() { return genres; }
  public String getHomepage() { return homepage; }
  public int getId() { return id; }
  public String getImdbId() { return imdbId; }
  public String getOriginalLanguage() { return originalLanguage; }
  public String getOriginalTitle() { return originalTitle; }
  public String getOverview() { return overview; }
  public double getPopularity() { return popularity; }
  public String getPosterPath() { return posterPath; }
  public List<ProductionCompany> getProductionCompanies() { return productionCompanies; }
  public List<ProductionCountry> getProductionCountries() { return productionCountries; }
  public String getReleaseDate() { return releaseDate; }
  public int getRevenue() { return revenue; }
  public int getRuntime() { return runtime; }
  public List<SpokenLanguage> getSpokenLanguages() { return spokenLanguages; }
  public String getStatus() { return status; }
  public String getTagline() { return tagline; }
  public String getTitle() { return title; }
  public boolean isVideo() { return video; }
  public double getVoteAverage() { return voteAverage; }
  public int getVoteCount() { return voteCount; }

  @Override public String toString() {
    return "Result{" +
        "adult=" + adult +
        ", backdropPath='" + backdropPath + '\'' +
        // ", belongsToCollection='" + belongsToCollection + '\'' +
        ", budget=" + budget +
        ", genres=" + genres +
        ", homepage='" + homepage + '\'' +
        ", id=" + id +
        ", imdbId='" + imdbId + '\'' +
        ", originalLanguage='" + originalLanguage + '\'' +
        ", originalTitle='" + originalTitle + '\'' +
        ", overview='" + overview + '\'' +
        ", popularity=" + popularity +
        ", posterPath='" + posterPath + '\'' +
        ", productionCompanies=" + productionCompanies +
        ", productionCountries=" + productionCountries +
        ", releaseDate='" + releaseDate + '\'' +
        ", revenue=" + revenue +
        ", runtime=" + runtime +
        ", spokenLanguages=" + spokenLanguages +
        ", status='" + status + '\'' +
        ", tagline='" + tagline + '\'' +
        ", title='" + title + '\'' +
        ", video=" + video +
        ", voteAverage=" + voteAverage +
        ", voteCount=" + voteCount +
        '}';
  }
}
