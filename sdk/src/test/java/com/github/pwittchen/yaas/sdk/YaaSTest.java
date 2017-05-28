package com.github.pwittchen.yaas.sdk;

import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;

public class YaaSTest {

  @Test
  public void shouldCreateYaaSObject() {
    // given
    YaaSProject project = mock(YaaSProject.class);

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

  @Test(expected = IllegalArgumentException.class)
  public void shouldNotCreateYaaSObjectWhenProjectIsNull() {
    // given
    YaaSProject project = null;
    Authorization authorization = mock(Authorization.class);

    // when
    new YaaS(project, authorization);

    // then exception is thrown
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldNotCreateYaaSObjectWhenAuthorizationIsNull() {
    // given
    YaaSProject project = mock(YaaSProject.class);
    Authorization authorization = null;

    // when
    new YaaS(project, authorization);

    // then exception is thrown
  }
}