package com.ramusthastudio.infomovies.model;

import java.util.Arrays;

public class Payload {
  private Events[] events;

  @Override public String toString() {
    return "Payload{" +
        "events=" + Arrays.toString(events) +
        '}';
  }
}
