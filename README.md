YaaS Java SDK
=============
[YaaS](https://yaas.io) (Hybris as a Service) SDK

This SDK allows to perform authorized requests to the microservices hidden behind YaaS proxy. 
Please note, this SDK is not an official SAP Hybris project and it does not cover all the features of YaaS.

Tech stack: Java 8, [Gradle](https://gradle.org/), [OkHttp3](http://square.github.io/okhttp/), [RxJava2](https://github.com/ReactiveX/RxJava) with [Reactive Streams](http://www.reactive-streams.org/), [Gson](https://github.com/google/gson)

Quick start
-----------

Exemplary usage of this SDK is as follows:

```java
YaaSProject project = new YaaSProject.Builder()
    .withClientId("YOUR_CLIENT_ID")
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

Client API
----------

`Client` interface supports basic HTTP methods like: `GET`, `POST`, `PUT` & `DELETE`.

```java
Flowable<Response> get(final String path);
Flowable<Response> post(final String path, final RequestBody body);
Flowable<Response> put(final String path, final RequestBody body);
Flowable<Response> delete(final String path, final RequestBody body);
Flowable<Response> delete(final String path);
```

References
----------
- [SAP Hybris](http://hybris.com/en/)
- [YaaS](https://yaas.io)