
package com.ramusthastudio.infomovies.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class DiscoverVideosMovies {
  @SerializedName("id")
  private int id;
  @SerializedName("results")
  private final List<ResultVideosMovies> resultVideosMovies = null;

  public int getId() { return id; }
  public List<ResultVideosMovies> getResultVideosMovies() { return resultVideosMovies; }

  @Override public String toString() {
    return "DiscoverVideosMovies{" +
        "id=" + id +
        ", resultVideosMovies=" + resultVideosMovies +
        '}';
  }
}
