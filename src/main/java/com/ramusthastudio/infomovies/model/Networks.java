package com.ramusthastudio.infomovies.model;

import com.google.gson.annotations.SerializedName;

public class Networks {
  @SerializedName("id")
  private int id;
  @SerializedName("name")
  private String name;

  public int getId() { return id; }
  public String getName() { return name; }

  public Networks setId(int aId) {
    id = aId;
    return this;
  }
  public Networks setName(String aName) {
    name = aName;
    return this;
  }

  @Override public String toString() {
    return "Networks{" +
        "id=" + id +
        ", name='" + name + '\'' +
        '}';
  }
}
