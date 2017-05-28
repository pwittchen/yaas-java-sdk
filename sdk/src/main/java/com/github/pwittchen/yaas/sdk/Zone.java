package com.github.pwittchen.yaas.sdk;

/**
 * Represents Zone of the microservice and its location (EU or US).
 */
public enum Zone {

  EU("eu"), US("us");

  private static final String API_URL_FORMAT = "https://api.%s.yaas.io/";
  private String url;

  Zone(final String name) {
    this.url = String.format(API_URL_FORMAT, name);
  }

  @Override public String toString() {
    return url;
  }
}
