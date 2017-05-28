package com.github.pwittchen.yaas.sdk;

/**
 * Represents an exception thrown during call to the YaaS
 */
public class YaaSException extends RuntimeException {

  public YaaSException(final String message) {
    super(message);
  }
}
