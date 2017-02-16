package com.ramusthastudio.infomovies.util;

public enum EGenre {
  ACTION(28, "Action"),
  ADVENTURE(12, "Adventure"),
  ANIMATION(16, "Animation"),
  COMEDY(35, "Comedy"),
  CRIME(80, "Crime"),
  DOCUMENTARY(99, "Documentary"),
  DRAMA(18, "Drama"),
  FAMILY(10751, "Family"),
  FANTASY(14, "Fantasy"),
  HISTORY(36, "History"),
  HORROR(27, "Horror"),
  MUSIC(10402, "Music"),
  MYSTERY(9648, "Mystery"),
  ROMANCE(10749, "Romance"),
  SCI_FI(878, "Science Fiction"),
  TV_MOVIE(10770, "Tv Movie"),
  THRILLER(53, "Thriller"),
  WAR(10752, "War"),
  WESTERN(37, "Western");

  private final int genre;
  private final String displayname;

  EGenre(int genre, String displayname) {
    this.genre = genre;
    this.displayname = displayname;
  }

  public int getGenre() { return genre; }
  public String getDisplayname() { return displayname; }
}
