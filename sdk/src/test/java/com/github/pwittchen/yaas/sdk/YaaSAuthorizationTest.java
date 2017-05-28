/*
 * Copyright (C) 2017 Piotr Wittchen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.pwittchen.yaas.sdk;

import java.io.IOException;
import java.util.Optional;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.internal.http.RealResponseBody;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;

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

  @Test
  public void shouldCreateAccessTokenRequest() {
    // given
    final YaaSAuthorization authorization = new YaaSAuthorization();
    final RequestBody body = mock(RequestBody.class);

    // when
    final Request request = authorization.createAccessTokenRequest(body);

    // then
    assertThat(request.method()).isEqualTo("POST");
    assertThat(request.url().toString()).isEqualTo("https://api.eu.yaas.io/hybris/oauth2/v1/token");
    assertThat(request.headers().value(0)).isEqualTo("application/x-www-form-urlencoded");
  }

  @Test
  public void shouldCreateAccessTokenRequestBody() {
    // given
    final YaaSAuthorization authorization = new YaaSAuthorization();
    final String grantTypeName ="grant_type";
    final String clientIdName = "client_id";
    final String clientSecretName = "client_secret";

    final String clientCredentials = "client_credentials";
    final String clientId = "testId";
    final String clientSecret = "testSecret";

    // when
    final FormBody body =
        authorization.createAccessTokenRequestBody(clientId, clientSecret);

    // then
    assertThat(body.name(0)).isEqualTo(grantTypeName);
    assertThat(body.name(1)).isEqualTo(clientIdName);
    assertThat(body.name(2)).isEqualTo(clientSecretName);

    assertThat(body.value(0)).isEqualTo(clientCredentials);
    assertThat(body.value(1)).isEqualTo(clientId);
    assertThat(body.value(2)).isEqualTo(clientSecret);
  }
}