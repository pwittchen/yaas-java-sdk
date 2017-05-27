package com.github.pwittchen.yaas.sdk;

import io.reactivex.Flowable;
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
   * @param project represents YaaSProject
   */
  public YaaS(final YaaSProject project) {
    this(project, new YaaSAuthorization(project.zone));
  }

  public YaaS(final YaaSProject project, Authorization authorization) {
    Preconditions.checkNotNull(project, "project == null");
    Preconditions.checkNotNull(authorization, "authorization == null");
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
  @Override public Flowable<Response> get(final String path) {
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
  @Override public Flowable<Response> post(final String path, final RequestBody body) {
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
  @Override public Flowable<Response> put(final String path, final RequestBody body) {
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
  @Override public Flowable<Response> delete(final String path, final RequestBody body) {
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
  @Override public Flowable<Response> delete(final String path) {
    return getAccessToken().flatMap(
        accessToken -> authorization.delete(accessToken, createUrl(path)));
  }

  private Flowable<String> getAccessToken() {
    return authorization.getAccessToken(project.clientId, project.clientSecret);
  }

  private String createUrl(final String path) {
    return String.format(URL_FORMAT, project.organization, project.service, project.version, path);
  }
}
