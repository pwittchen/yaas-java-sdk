package com.github.pwittchen.yaas.sdk;

import io.reactivex.Flowable;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * YaaS class can be used to perform authorized requests to services of the YaaS projects.
 */
public class YaaS implements Client {

  private final YaaSAuthorization authorization;
  private final YaaSProject project;

  /**
   * Create an instance of the YaaS object with provided project details.
   *
   * @param project represents YaaSProject
   */
  public YaaS(final YaaSProject project) {
    Preconditions.checkNotNull(project, "project == null");
    this.project = project;
    this.authorization = new YaaSAuthorization(project.zone);
  }

  /**
   * Perform an authorized GET request to the endpoint of the defined project through YaaS proxy
   * This method wraps OkHttp3 response via RxJava2 Observable. Response body can be accessed via
   * string() method performed on the Response object.
   *
   * @param endpointPath path to the concrete endpoint of the API hidden behind YaaS proxy
   * @return Flowable wrapping response object from OkHttp library
   */
  @Override public Flowable<Response> get(final String endpointPath) {
    return getAccessToken().flatMap(
        accessToken -> authorization.get(accessToken, createUrl(endpointPath)));
  }

  /**
   * Perform an authorized POST request to the endpoint of the defined project through YaaS proxy
   * This method wraps OkHttp3 response via RxJava2 Observable. Response body can be accessed via
   * string() method performed on the Response object.
   *
   * @param endpointPath path to the concrete endpoint of the API hidden behind YaaS proxy
   * @param body of the POST request
   * @return Flowable wrapping response object from OkHttp library
   */
  @Override public Flowable<Response> post(final String endpointPath, final RequestBody body) {
    return getAccessToken().flatMap(
        accessToken -> authorization.post(accessToken, createUrl(endpointPath), body));
  }

  /**
   * Perform an authorized PUT request to the endpoint of the defined project through YaaS proxy
   * This method wraps OkHttp3 response via RxJava2 Observable. Response body can be accessed via
   * string() method performed on the Response object.
   *
   * @param endpointPath path to the concrete endpoint of the API hidden behind YaaS proxy
   * @param body of the POST request
   * @return Flowable wrapping response object from OkHttp library
   */
  @Override public Flowable<Response> put(final String endpointPath, final RequestBody body) {
    return getAccessToken().flatMap(
        accessToken -> authorization.put(accessToken, createUrl(endpointPath), body));
  }

  /**
   * Perform an authorized DELETE request to the endpoint of the defined project through YaaS proxy
   * This method wraps OkHttp3 response via RxJava2 Observable. Response body can be accessed via
   * string() method performed on the Response object.
   *
   * @param endpointPath path to the concrete endpoint of the API hidden behind YaaS proxy
   * @param body of the POST request
   * @return Flowable wrapping response object from OkHttp library
   */
  @Override public Flowable<Response> delete(final String endpointPath, final RequestBody body) {
    return getAccessToken().flatMap(
        accessToken -> authorization.delete(accessToken, createUrl(endpointPath), body));
  }

  /**
   * Perform an authorized DELETE request to the endpoint of the defined project through YaaS proxy
   * This method wraps OkHttp3 response via RxJava2 Observable. Response body can be accessed via
   * string() method performed on the Response object.
   *
   * @param endpointPath path to the concrete endpoint of the API hidden behind YaaS proxy
   * @return Flowable wrapping response object from OkHttp library
   */
  @Override public Flowable<Response> delete(final String endpointPath) {
    return getAccessToken().flatMap(
        accessToken -> authorization.delete(accessToken, createUrl(endpointPath)));
  }

  private Flowable<String> getAccessToken() {
    return authorization.getAccessToken(project.clientId, project.clientSecret);
  }

  private String createUrl(final String endpointPath) {
    return String.format("%s/%s/%s/%s", project.organization, project.service, project.version,
        endpointPath);
  }
}
