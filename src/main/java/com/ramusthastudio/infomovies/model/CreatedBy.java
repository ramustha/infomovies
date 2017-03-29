package com.ramusthastudio.infomovies.model;

import com.google.gson.annotations.SerializedName;

public class CreatedBy {
  @SerializedName("id")
  private int id;
  @SerializedName("name")
  private String name;
  @SerializedName("profile_path")
  private String profilePath;

  public int getId() { return id; }
  public String getName() { return name; }
  public String getProfilePath() { return profilePath; }

  public CreatedBy setId(int aId) {
    id = aId;
    return this;
  }
  public CreatedBy setName(String aName) {
    name = aName;
    return this;
  }
  public CreatedBy setProfilePath(String aProfilePath) {
    profilePath = aProfilePath;
    return this;
  }

  @Override public String toString() {
    return "CreatedBy{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", profilePath='" + profilePath + '\'' +
        '}';
  }
}
