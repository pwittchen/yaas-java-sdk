package com.github.pwittchen.yaas.sdk;

import java.io.IOException;
import java.util.Optional;
import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okhttp3.internal.http.RealResponseBody;
import okio.BufferedSource;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class YaaSAuthorizationTest {

  @Test
  public void shouldCreateYaaSAuthorizationObjectWithNoParameters() {
    // when
    final Authorization authorization = new YaaSAuthorization();

    // then
    assertThat(authorization).isNotNull();
  }

  @Test
  public void shouldCreateYaaSAuthorizationObjectForZone() {
    // when
    final Authorization authorization = new YaaSAuthorization(Zone.EU);

    // then
    assertThat(authorization).isNotNull();
  }

  @Test
  public void shouldCreateYaaSAuthorizationObjectForAllParameters() {
    // given
    final Zone zone = Zone.EU;
    final Call.Factory client = mock(Call.Factory.class);
    final JsonConverter jsonConverter = mock(JsonConverter.class);

    // when
    final Authorization authorization = new YaaSAuthorization(zone, client, jsonConverter);

    // then
    assertThat(authorization).isNotNull();
  }

  @Test
  public void shouldRetrieveAccessToken() throws IOException {
    // given
    final String givenAccessToken = "023-018f03da-cdb7-4710-a4cf-70f89e23003f";
    final YaaSAuthorization authorization = new YaaSAuthorization();
    final String response =
        "{\"token_type\":\"Bearer\",\"access_token\":\"".concat(givenAccessToken)
            .concat("\",\"expires_in\":3600,\"scope\":\"hybris.tenant=pwtest\"}");
    final ResponseBody body = RealResponseBody.create(MediaType.parse("testMediaType"), response);

    // when
    final Optional<String> accessToken = authorization.retrieveAccessToken(body);

    // then
    assertThat(accessToken.isPresent()).isTrue();
    accessToken.ifPresent(token -> assertThat(token).isEqualTo(givenAccessToken));
  }
}