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

import io.reactivex.Single;
import java.util.Objects;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * YaaS class can be used to perform authorized requests to services of the YaaS projects.
 */
public class YaaS implements Client {

  private final static String URL_FORMAT = "%s/%s/%s/%s";
  private final Authorization authorization;
  private final YaaSProject project;

  /**
   * Create an instance of the YaaS object with provided project details.
   *
   * @param project represents YaaS Project
   */
  public YaaS(final YaaSProject project) {
    this(project, new YaaSAuthorization(project.zone));
  }

  /**
   * Create an instance of the YaaS object with provided project and authorization details
   *
   * @param project represents YaaS Project
   * @param authorization represents YaaS authorization
   */
  public YaaS(final YaaSProject project, final Authorization authorization) {
    Objects.requireNonNull(project, "project == null");
    Objects.requireNonNull(authorization, "authorization == null");
    this.project = project;
    this.authorization = authorization;
  }

  /**
   * Perform an authorized GET request to the endpoint of the defined project through YaaS proxy
   * This method wraps OkHttp3 response via RxJava2 Observable. Response body can be accessed via
   * string() method performed on the Response object.
   *
   * @param path path to the concrete endpoint of the API hidden behind YaaS proxy
   * @return Flowable wrapping response object from OkHttp library
   */
  @Override public Single<Response> get(final String path) {
    return getAccessToken().flatMap(accessToken -> authorization.get(accessToken, createUrl(path)));
  }

  /**
   * Perform an authorized POST request to the endpoint of the defined project through YaaS proxy
   * This method wraps OkHttp3 response via RxJava2 Observable. Response body can be accessed via
   * string() method performed on the Response object.
   *
   * @param path path to the concrete endpoint of the API hidden behind YaaS proxy
   * @param body of the POST request
   * @return Flowable wrapping response object from OkHttp library
   */
  @Override public Single<Response> post(final String path, final RequestBody body) {
    return getAccessToken().flatMap(
        accessToken -> authorization.post(accessToken, createUrl(path), body));
  }

  /**
   * Perform an authorized PUT request to the endpoint of the defined project through YaaS proxy
   * This method wraps OkHttp3 response via RxJava2 Observable. Response body can be accessed via
   * string() method performed on the Response object.
   *
   * @param path path to the concrete endpoint of the API hidden behind YaaS proxy
   * @param body of the POST request
   * @return Flowable wrapping response object from OkHttp library
   */
  @Override public Single<Response> put(final String path, final RequestBody body) {
    return getAccessToken().flatMap(
        accessToken -> authorization.put(accessToken, createUrl(path), body));
  }

  /**
   * Perform an authorized DELETE request to the endpoint of the defined project through YaaS proxy
   * This method wraps OkHttp3 response via RxJava2 Observable. Response body can be accessed via
   * string() method performed on the Response object.
   *
   * @param path path to the concrete endpoint of the API hidden behind YaaS proxy
   * @param body of the POST request
   * @return Flowable wrapping response object from OkHttp library
   */
  @Override public Single<Response> delete(final String path, final RequestBody body) {
    return getAccessToken().flatMap(
        accessToken -> authorization.delete(accessToken, createUrl(path), body));
  }

  /**
   * Perform an authorized DELETE request to the endpoint of the defined project through YaaS proxy
   * This method wraps OkHttp3 response via RxJava2 Observable. Response body can be accessed via
   * string() method performed on the Response object.
   *
   * @param path path to the concrete endpoint of the API hidden behind YaaS proxy
   * @return Flowable wrapping response object from OkHttp library
   */
  @Override public Single<Response> delete(final String path) {
    return getAccessToken().flatMap(
        accessToken -> authorization.delete(accessToken, createUrl(path)));
  }

  private Single<String> getAccessToken() {
    return authorization.getAccessToken(project.clientId, project.clientSecret);
  }

  private String createUrl(final String path) {
    return String.format(URL_FORMAT, project.organization, project.service, project.version, path);
  }
}
