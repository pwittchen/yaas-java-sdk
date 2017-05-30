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

import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;

public class YaaSTest {

  @Test
  public void shouldCreateYaaSObject() {
    // given
    YaaSProject project =
        new YaaSProject(Zone.EU, "org", "service", "v1", "clientId", "clientSecret");

    // when
    Client client = new YaaS(project);

    // then
    assertThat(client).isNotNull();
  }

  @Test
  public void shouldCreateYaaSObjectWithAuthorization() {
    // given
    YaaSProject project = mock(YaaSProject.class);
    Authorization authorization = mock(Authorization.class);

    // when
    Client client = new YaaS(project, authorization);

    // then
    assertThat(client).isNotNull();
  }

  @Test(expected = NullPointerException.class)
  public void shouldNotCreateYaaSObjectWhenProjectIsNull() {
    // given
    YaaSProject project = null;
    Authorization authorization = mock(Authorization.class);

    // when
    new YaaS(project, authorization);

    // then exception is thrown
  }

  @Test(expected = NullPointerException.class)
  public void shouldNotCreateYaaSObjectWhenAuthorizationIsNull() {
    // given
    YaaSProject project = mock(YaaSProject.class);
    Authorization authorization = null;

    // when
    new YaaS(project, authorization);

    // then exception is thrown
  }
}