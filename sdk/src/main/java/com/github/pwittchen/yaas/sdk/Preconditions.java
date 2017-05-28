package com.github.pwittchen.yaas.sdk;

/**
 * Preconditions utility class
 */
public class Preconditions {

  private Preconditions() {
  }

  public static void checkNotNull(final Object object, final String message) {
    if (object == null) {
      throw new IllegalArgumentException(message);
    }
  }
}
