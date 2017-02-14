package com.ramusthastudio.infomovies.model;

import com.google.gson.annotations.SerializedName;

public class ProductionCountry {
  @SerializedName("iso_3166_1")
  private String iso31661;
  @SerializedName("name")
  private String name;

  public String getIso31661() { return iso31661; }
  public String getName() { return name; }

  @Override public String toString() {
    return "ProductionCountry{" +
        "iso31661='" + iso31661 + '\'' +
        ", name='" + name + '\'' +
        '}';
  }
}
