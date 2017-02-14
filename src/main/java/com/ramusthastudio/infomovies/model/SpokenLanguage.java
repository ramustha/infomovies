package com.ramusthastudio.infomovies.model;

import com.google.gson.annotations.SerializedName;

public class SpokenLanguage {
  @SerializedName("iso_639_1")
  private String iso6391;
  @SerializedName("name")
  private String name;

  public String getIso6391() { return iso6391; }
  public String getName() { return name; }

  @Override public String toString() {
    return "SpokenLanguage{" +
        "iso6391='" + iso6391 + '\'' +
        ", name='" + name + '\'' +
        '}';
  }
}
