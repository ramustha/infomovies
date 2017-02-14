package com.ramusthastudio.infomovies.model;

public class Events {
  private String type;
  private String replyToken;
  private Source source;
  private long timestamp;
  private Message message;

  public String type() { return type; }
  public String replyToken() { return replyToken; }
  public Source source() { return source; }
  public long timestamp() { return timestamp; }
  public Message message() { return message; }

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
