package com.ramusthastudio.infomovies.model;

public class Source {
  private String userId;
  private String groupId;
  private String roomId;
  private String type;

  public String userId() { return userId; }
  public String groupId() { return groupId; }
  public String roomId() { return roomId; }
  public String type() { return type; }

  @Override public String toString() {
    return "Source{" +
        "userId='" + userId + '\'' +
        ", groupId='" + groupId + '\'' +
        ", roomId='" + roomId + '\'' +
        ", type='" + type + '\'' +
        '}';
  }
}
