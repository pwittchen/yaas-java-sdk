package com.github.pwittchen.yaas.sdk;

import java.lang.reflect.Type;

/**
 * Converter used to convert data from JSON to POJO and backwards
 */
public interface JsonConverter {
  <T> T fromJson(String json, Type typeOfT);

  <T> String toJson(T object, Type typeOfT);
}
