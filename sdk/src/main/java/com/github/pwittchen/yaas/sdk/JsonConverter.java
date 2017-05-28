package com.github.pwittchen.yaas.sdk;

import java.lang.reflect.Type;

public interface JsonConverter {
  <T> T fromJson(String json, Type typeOfT);

  <T> String toJson(T object, Type typeOfT);
}
