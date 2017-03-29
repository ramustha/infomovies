package com.ramusthastudio.infomovies.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ResultTvsVideo {
  @SerializedName("id")
  private int id;
  @SerializedName("results")
  private List<ResultVideo> resultVideo = null;

  public int getId() { return id; }
  public List<ResultVideo> getResultVideo() { return resultVideo; }

  public ResultTvsVideo setId(int aId) {
    id = aId;
    return this;
  }
  public ResultTvsVideo setResultVideo(List<ResultVideo> aResultVideo) {
    resultVideo = aResultVideo;
    return this;
  }

  @Override public String toString() {
    return "ResultTvsVideo{" +
        "id=" + id +
        ", resultVideo=" + resultVideo.size() +
        '}';
  }
}
