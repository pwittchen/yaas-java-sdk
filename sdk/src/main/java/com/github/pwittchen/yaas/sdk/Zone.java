package com.github.pwittchen.yaas.sdk;

public enum Zone {
  EU("eu"), US("us");

  private static final String API_URL_FORMAT = "https://api.%s.yaas.io/";
  private String url;

  Zone(String name) {
    this.url = String.format(API_URL_FORMAT, name);
  }

  @Override public String toString() {
    return url;
  }
}
