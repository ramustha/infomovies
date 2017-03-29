package com.ramusthastudio.infomovies.model;

import com.google.gson.annotations.SerializedName;

public class ResultVideo {
  @SerializedName("id")
  private String id;
  @SerializedName("iso_639_1")
  private String iso_639_1;
  @SerializedName("iso_3166_1")
  private String iso_3166_1;
  @SerializedName("key")
  private String key;
  @SerializedName("name")
  private String name;
  @SerializedName("site")
  private String site;
  @SerializedName("size")
  private int size;
  @SerializedName("type")
  private String type;

  public String getId() { return id; }
  public String getIso_639_1() { return iso_639_1; }
  public String getIso_3166_1() { return iso_3166_1; }
  public String getKey() { return key; }
  public String getName() { return name; }
  public String getSite() { return site; }
  public int getSize() { return size; }
  public String getType() { return type; }

  public ResultVideo setId(String aId) {
    id = aId;
    return this;
  }
  public ResultVideo setIso_639_1(String aIso_639_1) {
    iso_639_1 = aIso_639_1;
    return this;
  }
  public ResultVideo setIso_3166_1(String aIso_3166_1) {
    iso_3166_1 = aIso_3166_1;
    return this;
  }
  public ResultVideo setKey(String aKey) {
    key = aKey;
    return this;
  }
  public ResultVideo setName(String aName) {
    name = aName;
    return this;
  }
  public ResultVideo setSite(String aSite) {
    site = aSite;
    return this;
  }
  public ResultVideo setSize(int aSize) {
    size = aSize;
    return this;
  }
  public ResultVideo setType(String aType) {
    type = aType;
    return this;
  }

  @Override public String toString() {
    return "ResultVideo{" +
        "id='" + id + '\'' +
        ", iso_639_1='" + iso_639_1 + '\'' +
        ", iso_3166_1='" + iso_3166_1 + '\'' +
        ", key='" + key + '\'' +
        ", name='" + name + '\'' +
        ", site='" + site + '\'' +
        ", size=" + size +
        ", type='" + type + '\'' +
        '}';
  }
}
