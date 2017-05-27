YaaS Java SDK
=============
[YaaS](https://yaas.io) (Hybris as a Service) SDK

This SDK allows to perform authorized requests to the services hidden behind YaaS proxy. 
Please note, this SDK is not official SAP Hybris project and it does not cover all features of YaaS.

Tech stack: Java 8, Gradle, OkHttp3, RxJava2 with Reactive Streams, Gson

Example
-------

Exemplary usage of this SDK is as follows:

```java
    YaaSProject project = new YaaSProject.Builder().withClientId("YOUR_CLIENT_ID")
        .withClientSecret("YOUR_CLIENT_SECRET")
        .withOrganization("YOUR_ORGANIZATION")
        .withService("YOUR_SERVICE")
        .withVersion("v1")
        .withZone(Zone.EU)
        .build();

    Client client = new YaaS(project);

    client.get("path/to/your/endpoint")
        .subscribeOn(Schedulers.newThread())
        .blockingSubscribe(response -> System.out.println(response.body().string()));
```
