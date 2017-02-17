package com.ramusthastudio.infomovies.model;

public final class FindMovies {
  private String title;
  private int year;
  private String region;
  private int page;
  private int max;
  private String flag;

  private FindMovies() { }

  public String getTitle() {
    return title;
  }
  public String getRegion() {
    return region;
  }
  public int getYear() {
    return year;
  }
  public int getPage() { return page; }
  public int getMax() { return max; }
  public String getFlag() { return flag; }

  public FindMovies withTitle(String aTitle) {
    title = aTitle;
    return this;
  }
  public FindMovies withYear(int aYear) {
    year = aYear;
    return this;
  }
  public FindMovies withRegion(String aRegion) {
    region = aRegion;
    return this;
  }
  public FindMovies withPage(int aMin) {
    page = aMin;
    return this;
  }
  public FindMovies withMax(int aMax) {
    max = aMax;
    return this;
  }
  public FindMovies withFlag(String aFlag) {
    flag = aFlag;
    return this;
  }

  public static FindMovies newFindMovies() {
    return new FindMovies();
  }

  @Override public String toString() {
    return "FindMovies{" +
        "title='" + title + '\'' +
        ", year=" + year +
        ", region='" + region + '\'' +
        ", page=" + page +
        ", max=" + max +
        ", flag='" + flag + '\'' +
        '}';
  }
}
