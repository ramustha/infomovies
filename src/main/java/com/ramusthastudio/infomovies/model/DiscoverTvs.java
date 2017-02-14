
package com.ramusthastudio.infomovies.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class DiscoverTvs {
  @SerializedName("page")
  private int page;
  @SerializedName("results")
  private final List<ResultTvs> fResultTvs = null;
  @SerializedName("total_results")
  private int totalResults;
  @SerializedName("total_pages")
  private int totalPages;

  public int getPage() { return page; }
  public List<ResultTvs> getDiscoverresults() { return fResultTvs; }
  public int getTotalResults() { return totalResults; }
  public int getTotalPages() { return totalPages; }

  @Override public String toString() {
    return "Discover{" +
        "page=" + page +
        ", Discoverresults=" + fResultTvs +
        ", totalResults=" + totalResults +
        ", totalPages=" + totalPages +
        '}';
  }
}
