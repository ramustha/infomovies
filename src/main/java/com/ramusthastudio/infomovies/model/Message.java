package com.ramusthastudio.infomovies.model;

public class Message {
  private String type;
  private String id;
  private String text;

  @Override public String toString() {
    return "Message{" +
        "type='" + type + '\'' +
        ", id='" + id + '\'' +
        ", text='" + text + '\'' +
        '}';
  }
}
