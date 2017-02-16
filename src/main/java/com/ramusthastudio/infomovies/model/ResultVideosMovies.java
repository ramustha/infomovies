package com.ramusthastudio.infomovies.model;

import com.google.gson.annotations.SerializedName;

public class ResultVideosMovies {
  @SerializedName("id")
  private String id;
  @SerializedName("key")
  private String key;
  @SerializedName("site")
  private String site;
  @SerializedName("type")
  private String type;

  public String getId() { return id; }
  public String getKey() { return key; }
  public String getSite() { return site; }
  public String getType() { return type; }

  @Override public String toString() {
    return "ResultVideosMovie{" +
        "id='" + id + '\'' +
        ", key='" + key + '\'' +
        ", site='" + site + '\'' +
        ", type='" + type + '\'' +
        '}';
  }
}
