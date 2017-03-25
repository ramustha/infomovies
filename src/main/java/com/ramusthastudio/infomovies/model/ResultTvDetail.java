package com.ramusthastudio.infomovies.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ResultTvDetail {
  @SerializedName("backdrop_path")
  private String backdropPath;
  @SerializedName("created_by")
  private List<CreatedBy> createdBy = null;
  @SerializedName("episode_run_time")
  private List<Integer> episodeRunTimes = null;
  @SerializedName("first_air_date")
  private String firstAirDate;
  @SerializedName("genres")
  private final List<Genre> genres = null;
  @SerializedName("homepage")
  private String homepage;
  @SerializedName("id")
  private int id;
  @SerializedName("in_production")
  private boolean inProduction;
  @SerializedName("languages")
  private List<String> languages = null;
  @SerializedName("last_air_date")
  private String lastAirDate;
  @SerializedName("name")
  private String name;
  @SerializedName("networks")
  private List<Networks> networks = null;
  @SerializedName("number_of_episodes")
  private int numberOfEpisodes;
  @SerializedName("number_of_seasons")
  private int numberOfSeasons;
  @SerializedName("origin_country")
  private List<String> originCountry = null;
  @SerializedName("original_language")
  private String originalLanguage;
  @SerializedName("original_name")
  private String originalName;
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
  @SerializedName("seasons")
  private final List<Seasons> seasons = null;
  @SerializedName("status")
  private String status;
  @SerializedName("type")
  private String type;
  @SerializedName("vote_average")
  private double voteAverage;
  @SerializedName("vote_count")
  private int voteCount;

  public String getBackdropPath() { return backdropPath; }
  public List<CreatedBy> getCreatedBy() { return createdBy; }
  public List<Integer> getEpisodeRunTimes() { return episodeRunTimes; }
  public String getFirstAirDate() { return firstAirDate; }
  public List<Genre> getGenres() { return genres; }
  public String getHomepage() { return homepage; }
  public int getId() { return id; }
  public boolean isInProduction() { return inProduction; }
  public List<String> getLanguages() { return languages; }
  public String getLastAirDate() { return lastAirDate; }
  public String getName() { return name; }
  public List<Networks> getNetworks() { return networks; }
  public int getNumberOfEpisodes() { return numberOfEpisodes; }
  public int getNumberOfSeasons() { return numberOfSeasons; }
  public List<String> getOriginCountry() { return originCountry; }
  public String getOriginalLanguage() { return originalLanguage; }
  public String getOriginalName() { return originalName; }
  public String getOverview() { return overview; }
  public double getPopularity() { return popularity; }
  public String getPosterPath() { return posterPath; }
  public List<ProductionCompany> getProductionCompanies() { return productionCompanies; }
  public List<ProductionCountry> getProductionCountries() { return productionCountries; }
  public List<Seasons> getSeasons() { return seasons; }
  public String getStatus() { return status; }
  public String getType() { return type; }
  public double getVoteAverage() { return voteAverage; }
  public int getVoteCount() { return voteCount; }

  public ResultTvDetail setBackdropPath(String aBackdropPath) {
    backdropPath = aBackdropPath;
    return this;
  }
  public ResultTvDetail setCreatedBy(List<CreatedBy> aCreatedBy) {
    createdBy = aCreatedBy;
    return this;
  }
  public ResultTvDetail setEpisodeRunTimes(List<Integer> aEpisodeRunTimes) {
    episodeRunTimes = aEpisodeRunTimes;
    return this;
  }
  public ResultTvDetail setFirstAirDate(String aFirstAirDate) {
    firstAirDate = aFirstAirDate;
    return this;
  }
  public ResultTvDetail setHomepage(String aHomepage) {
    homepage = aHomepage;
    return this;
  }
  public ResultTvDetail setId(int aId) {
    id = aId;
    return this;
  }
  public ResultTvDetail setInProduction(boolean aInProduction) {
    inProduction = aInProduction;
    return this;
  }
  public ResultTvDetail setLanguages(List<String> aLanguages) {
    languages = aLanguages;
    return this;
  }
  public ResultTvDetail setLastAirDate(String aLastAirDate) {
    lastAirDate = aLastAirDate;
    return this;
  }
  public ResultTvDetail setName(String aName) {
    name = aName;
    return this;
  }
  public ResultTvDetail setNetworks(List<Networks> aNetworks) {
    networks = aNetworks;
    return this;
  }
  public ResultTvDetail setNumberOfEpisodes(int aNumberOfEpisodes) {
    numberOfEpisodes = aNumberOfEpisodes;
    return this;
  }
  public ResultTvDetail setNumberOfSeasons(int aNumberOfSeasons) {
    numberOfSeasons = aNumberOfSeasons;
    return this;
  }
  public ResultTvDetail setOriginCountry(List<String> aOriginCountry) {
    originCountry = aOriginCountry;
    return this;
  }
  public ResultTvDetail setOriginalLanguage(String aOriginalLanguage) {
    originalLanguage = aOriginalLanguage;
    return this;
  }
  public ResultTvDetail setOriginalName(String aOriginalName) {
    originalName = aOriginalName;
    return this;
  }
  public ResultTvDetail setOverview(String aOverview) {
    overview = aOverview;
    return this;
  }
  public ResultTvDetail setPopularity(double aPopularity) {
    popularity = aPopularity;
    return this;
  }
  public ResultTvDetail setPosterPath(String aPosterPath) {
    posterPath = aPosterPath;
    return this;
  }
  public ResultTvDetail setStatus(String aStatus) {
    status = aStatus;
    return this;
  }
  public ResultTvDetail setType(String aType) {
    type = aType;
    return this;
  }
  public ResultTvDetail setVoteAverage(double aVoteAverage) {
    voteAverage = aVoteAverage;
    return this;
  }
  public ResultTvDetail setVoteCount(int aVoteCount) {
    voteCount = aVoteCount;
    return this;
  }

  @Override public String toString() {
    return "ResultTvDetail{" +
        "backdropPath='" + backdropPath + '\'' +
        ", createdBy=" + createdBy +
        ", episodeRunTimes=" + episodeRunTimes +
        ", firstAirDate='" + firstAirDate + '\'' +
        ", genres=" + genres +
        ", homepage='" + homepage + '\'' +
        ", id=" + id +
        ", inProduction=" + inProduction +
        ", languages=" + languages +
        ", lastAirDate='" + lastAirDate + '\'' +
        ", name='" + name + '\'' +
        ", networks=" + networks +
        ", numberOfEpisodes=" + numberOfEpisodes +
        ", numberOfSeasons=" + numberOfSeasons +
        ", originCountry=" + originCountry +
        ", originalLanguage='" + originalLanguage + '\'' +
        ", originalName='" + originalName + '\'' +
        ", overview='" + overview + '\'' +
        ", popularity=" + popularity +
        ", posterPath='" + posterPath + '\'' +
        ", productionCompanies=" + productionCompanies +
        ", productionCountries=" + productionCountries +
        ", seasons=" + seasons +
        ", status='" + status + '\'' +
        ", type='" + type + '\'' +
        ", voteAverage=" + voteAverage +
        ", voteCount=" + voteCount +
        '}';
  }
}
