package com.ramusthastudio.infomovies.model;

import com.google.gson.annotations.SerializedName;

public class ProductionCompany {
  @SerializedName("name")
  private String name;
  @SerializedName("id")
  private int id;

  public String getName() { return name; }
  public int getId() { return id; }

  @Override public String toString() {
    return "ProductionCompany{" +
        "name='" + name + '\'' +
        ", id=" + id +
        '}';
  }
}
