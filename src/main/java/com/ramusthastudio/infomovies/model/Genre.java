package com.ramusthastudio.infomovies.model;

import com.google.gson.annotations.SerializedName;

public class Genre {
  @SerializedName("id")
  private int id;
  @SerializedName("name")
  private String name;

  public int getId() { return id; }
  public String getName() { return name; }

  @Override public String toString() {
    return "Genre{" +
        "id=" + id +
        ", name='" + name + '\'' +
        '}';
  }
}
