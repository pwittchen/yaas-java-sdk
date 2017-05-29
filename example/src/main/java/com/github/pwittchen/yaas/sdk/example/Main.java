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
package com.github.pwittchen.yaas.sdk.example;

import com.github.pwittchen.yaas.sdk.Client;
import com.github.pwittchen.yaas.sdk.YaaS;
import com.github.pwittchen.yaas.sdk.YaaSProject;
import com.github.pwittchen.yaas.sdk.Zone;

public class Main {
  public static void main(String args[]) {

    // This is the simplest example of using yaas-java-sdk
    // Provide your configuration below and start the application

    YaaSProject project = new YaaSProject.Builder()
        .withClientId("YOUR_CLIENT_ID")
        .withClientSecret("YOUR_CLIENT_SECRET")
        .withOrganization("YOUR_ORGANIZATION")
        .withService("YOUR_SERVICE")
        .withVersion("v1")
        .withZone(Zone.EU)
        .build();

    Client client = new YaaS(project);

    client.get("data/getData/5313")
        .doFinally(() -> System.exit(0))
        .subscribe(response -> System.out.println(response.body().string()));
  }
}
