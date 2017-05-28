package com.github.pwittchen.yaas.sdk;

import org.junit.Test;

public class PreconditionsTest {

  @Test(expected = IllegalArgumentException.class)
  public void shouldThrowAnExceptionWhenObjectIsNull() throws Exception {
    // given
    Object object = null;
    String message = "object == null";

    // when
    Preconditions.checkNotNull(object, message);

    // then throw an exception
  }
}