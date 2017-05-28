package com.github.pwittchen.yaas.sdk;

import org.junit.Before;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class GsonConverterTest {
  private JsonConverter jsonConverter;

  @Before
  public void setUp() {
    this.jsonConverter = new GsonConverter();
  }

  @Test
  public void shouldConvertFromJsonToPojo() throws Exception {
    // given
    final int givenId = 5312;
    final String givenName = "test";
    final String json = "{\n"
        + "    \"id\": ".concat(String.valueOf(givenId)).concat(",\n")
        + "    \"name\": \"".concat(givenName).concat("\"\n")
        + "  }";

    // when
    final TestObject object = jsonConverter.fromJson(json, TestObject.class);

    // then
    assertThat(object.id).isEqualTo(givenId);
    assertThat(object.name).isEqualTo(givenName);
  }

  @Test
  public void shouldConvertToJsonFromPojo() throws Exception {
    // given
    final int givenId = 5312;
    final String givenName = "test";
    final TestObject object = new TestObject(givenId, givenName);
    final String expectedJson = "{"
        + "\"id\":".concat(String.valueOf(givenId)).concat(",")
        + "\"name\":\"".concat(givenName).concat("\"")
        + "}";

    // when
    final String json = jsonConverter.toJson(object, TestObject.class);

    // then
    assertThat(json).isEqualTo(expectedJson);
  }

  private class TestObject {
    final int id;
    final String name;

    TestObject(final int id, final String name) {
      this.id = id;
      this.name = name;
    }
  }
}