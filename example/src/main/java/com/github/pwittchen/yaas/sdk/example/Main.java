package com.github.pwittchen.yaas.sdk.example;

import com.github.pwittchen.yaas.sdk.Client;
import com.github.pwittchen.yaas.sdk.YaaS;
import com.github.pwittchen.yaas.sdk.YaaSProject;
import com.github.pwittchen.yaas.sdk.Zone;
import io.reactivex.schedulers.Schedulers;

public class Main {
  public static void main(String args[]) {

    YaaSProject project = new YaaSProject.Builder().withClientId("YOUR_CLIENT_ID")
        .withClientSecret("YOUR_CLIENT_SECRET")
        .withOrganization("pwittchen")
        .withService("airqualitypoland")
        .withVersion("v1")
        .withZone(Zone.EU)
        .build();

    Client client = new YaaS(project);

    client.get("station/findAll")
        .subscribeOn(Schedulers.newThread())
        .blockingSubscribe(response -> System.out.println(response.body().string()));

    System.exit(0);
  }
}
