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

/**
 * Represents YaaS project, which is defined in the YaaS Builder web app
 */
public class YaaSProject {

  public final Zone zone;
  public final String organization;
  public final String service; // AKA tenant or Project Identifier
  public final String version;
  public final String clientId;
  public final String clientSecret;

  public YaaSProject(Zone zone, String organization, String service, String version,
      String clientId, String clientSecret) {
    this.zone = zone;
    this.organization = organization;
    this.service = service;
    this.version = version;
    this.clientId = clientId;
    this.clientSecret = clientSecret;
  }

  public static class Builder {
    private Zone zone;
    private String organization;
    private String service;
    private String version;
    private String clientId;
    private String clientSecret;

    public Builder withZone(final Zone zone) {
      this.zone = zone;
      return this;
    }

    public Builder withOrganization(final String organization) {
      this.organization = organization;
      return this;
    }

    public Builder withService(final String service) {
      this.service = service;
      return this;
    }

    public Builder withVersion(final String version) {
      this.version = version;
      return this;
    }

    public Builder withClientId(final String clientId) {
      this.clientId = clientId;
      return this;
    }

    public Builder withClientSecret(final String clientSecret) {
      this.clientSecret = clientSecret;
      return this;
    }

    public YaaSProject build() {
      return new YaaSProject(zone, organization, service, version, clientId, clientSecret);
    }
  }
}
