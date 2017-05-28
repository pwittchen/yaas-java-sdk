package com.github.pwittchen.yaas.sdk;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.lang.reflect.Type;

public class GsonConverter implements JsonConverter {
  private final Gson gson;

  public GsonConverter() {
    this.gson =
        new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();
  }

  @Override public <T> T fromJson(String json, Type typeOfT) {
    return gson.fromJson(json, typeOfT);
  }

  @Override public <T> String toJson(T object, Type typeOfT) {
    return gson.toJson(object, typeOfT);
  }
}
