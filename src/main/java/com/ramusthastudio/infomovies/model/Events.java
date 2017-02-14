package com.ramusthastudio.infomovies.model;

public class Events {
  private String type;
  private String replyToken;
  private Source source;
  private long timestamp;
  private Message message;

  @Override public String toString() {
    return "Events{" +
        "type='" + type + '\'' +
        ", replyToken='" + replyToken + '\'' +
        ", source=" + source +
        ", timestamp=" + timestamp +
        ", message=" + message +
        '}';
  }
}
