package com.github.pwittchen.yaas.sdk;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import java.io.IOException;
import java.util.Optional;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class YaaSAuthorization implements Authorization {

  private static final String BODY_CLIENT_ID = "client_id";
  private static final String BODY_CLIENT_SECRET = "client_secret";
  private static final String BODY_GRANT_TYPE = "grant_type";
  private static final String BODY_CLIENT_CREDENTIALS = "client_credentials";
  private static final String CONTENT_TYPE = "Content-Type";
  private static final String CONTENT_TYPE_FORM_URLENCODED = "application/x-www-form-urlencoded";
  private static final String CONTENT_TYPE_APPLICATION_JSON = "application/json";
  private static final String HEADER_AUTHORIZATION = "Authorization";
  private static final String HEADER_BEARER = "Bearer";
  private static final String OAUTH2_TOKEN_URL = "/hybris/oauth2/v1/token";
  private static final String SPACE = " ";

  private final Zone zone;
  private final Call.Factory client;
  private final JsonConverter jsonConverter;

  /**
   * Creates YaaS Authorization object with default Zone.EU
   */
  public YaaSAuthorization() {
    this(Zone.EU);
  }

  /**
   * Creates YaaS Authorization object
   *
   * @param zone of the microservice (EU or US)
   */
  public YaaSAuthorization(final Zone zone) {
    this(zone, new OkHttpClient(), new GsonConverter());
  }

  /**
   * Creates YaaS Authorization object
   *
   * @param zone of the microservice (EU or US)
   * @param client Client.Factory interface from OkHttpClient, OkHttpClient class implements it
   * @param jsonConverter interface for classes performing conversion from JSON to POJO and
   * backwards
   */
  public YaaSAuthorization(final Zone zone, final Call.Factory client,
      final JsonConverter jsonConverter) {
    Preconditions.checkNotNull(zone, "zone == null");
    Preconditions.checkNotNull(client, "client == null");
    Preconditions.checkNotNull(jsonConverter, "jsonConverter == null");
    this.zone = zone;
    this.client = client;
    this.jsonConverter = jsonConverter;
  }

  /**
   * Reads Access Token from the YaaS. Later it can be used as a Bearer in Authorization header for
   * making secure request to the microservices.
   *
   * @param clientId id of the client read from YaaS Builder web app
   * @param clientSecret secret value of the client read form YaaS Builder web app
   * @return Flowable wrapping String which is an Access Token (AKA Bearer)
   */
  @Override
  public Flowable<String> getAccessToken(final String clientId, final String clientSecret) {
    final FormBody requestBody = createAccessTokenRequestBody(clientId, clientSecret);
    final Request request = createAccessTokenRequest(requestBody);

    return Flowable.create(emitter -> client.newCall(request).enqueue(new Callback() {
      @Override public void onFailure(Call call, IOException e) {
        emitter.onError(e);
        emitter.onComplete();
      }

      @Override public void onResponse(final Call call, final Response response) {
        final Optional<ResponseBody> body = readResponseBody(response.body());
        if (body.isPresent()) {
          final Optional<String> accessToken = retrieveAccessToken(body.get());
          if (accessToken.isPresent()) {
            emitter.onNext(accessToken.get());
          } else {
            emitter.onError(new YaaSException("Access Token is empty"));
          }
        } else {
          emitter.onError(new YaaSException("ResponseBody is empty"));
        }
        emitter.onComplete();
      }
    }), BackpressureStrategy.BUFFER);
  }

  protected Request createAccessTokenRequest(final RequestBody requestBody) {
    return new Request.Builder().url(zone.toString().concat(OAUTH2_TOKEN_URL))
        .addHeader(CONTENT_TYPE, CONTENT_TYPE_FORM_URLENCODED)
        .post(requestBody)
        .build();
  }

  protected FormBody createAccessTokenRequestBody(final String clientId,
      final String clientSecret) {
    return new FormBody.Builder().add(BODY_GRANT_TYPE, BODY_CLIENT_CREDENTIALS)
        .add(BODY_CLIENT_ID, clientId)
        .add(BODY_CLIENT_SECRET, clientSecret)
        .build();
  }

  protected Optional<String> retrieveAccessToken(final ResponseBody responseBody) {
    Optional<String> body = tryToReadResponseBodyString(responseBody);

    if (!body.isPresent()) {
      return Optional.empty();
    }

    YaaSAuthorizationResponse response =
        jsonConverter.fromJson(body.get(), YaaSAuthorizationResponse.class);

    if (response != null && !response.accessToken.isEmpty()) {
      return Optional.of(response.accessToken);
    }

    return Optional.empty();
  }

  private Optional<String> tryToReadResponseBodyString(final ResponseBody body) {
    try {
      return Optional.of(body.string());
    } catch (IOException e) {
      return Optional.empty();
    }
  }

  private Optional<ResponseBody> readResponseBody(final ResponseBody body) {
    if (body == null) {
      return Optional.empty();
    } else {
      return Optional.of(body);
    }
  }

  /**
   * Performs HTTP GET request to the service
   *
   * @param bearer id (Access Token)
   * @param path of the endpoint
   * @return Flowable with the Response
   */
  @Override public Flowable<Response> get(final String bearer, final String path) {
    return request(createAuthorizedGetRequest(bearer, path));
  }

  /**
   * Performs HTTP POST request to the service
   *
   * @param bearer id (Access Token)
   * @param path of the endpoint
   * @param body of the request
   * @return Flowable with the Response
   */
  @Override
  public Flowable<Response> post(final String bearer, final String path, final RequestBody body) {
    return request(createAuthorizedPostRequest(bearer, path, body));
  }

  /**
   * Performs HTTP PUT request to the service
   *
   * @param bearer id (Access Token)
   * @param path of the endpoint
   * @param body of the request
   * @return Flowable with the Response
   */
  @Override
  public Flowable<Response> put(final String bearer, final String path, final RequestBody body) {
    return request(createAuthorizedPutRequest(bearer, path, body));
  }

  /**
   * Performs HTTP DELETE request to the service
   *
   * @param bearer id (Access Token)
   * @param path of the endpoint
   * @param body of the request
   * @return Flowable with the Response
   */
  @Override
  public Flowable<Response> delete(final String bearer, final String path, final RequestBody body) {
    return request(createAuthorizedDeleteRequest(bearer, path, body));
  }

  /**
   * Performs HTTP DELETE request to the service
   *
   * @param bearer id (Access Token)
   * @param path of the endpoint
   * @return Flowable with the Response
   */
  @Override public Flowable<Response> delete(final String bearer, final String path) {
    return request(createAuthorizedDeleteRequest(bearer, path));
  }

  protected Flowable<Response> request(final Request request) {
    return Flowable.create(emitter -> client.newCall(request).enqueue(new Callback() {
      @Override public void onFailure(Call call, IOException e) {
        emitter.onError(e);
        emitter.onComplete();
      }

      @Override public void onResponse(Call call, Response response) throws IOException {
        emitter.onNext(response);
        emitter.onComplete();
      }
    }), BackpressureStrategy.BUFFER);
  }

  protected Request createAuthorizedGetRequest(final String bearer, final String path) {
    return createRequestBuilder(bearer, path).get().build();
  }

  protected Request createAuthorizedPostRequest(final String bearer, final String path,
      final RequestBody body) {
    return createRequestBuilder(bearer, path).post(body).build();
  }

  protected Request createAuthorizedPutRequest(final String bearer, final String path,
      final RequestBody body) {
    return createRequestBuilder(bearer, path).put(body).build();
  }

  protected Request createAuthorizedDeleteRequest(final String bearer, final String path,
      final RequestBody body) {
    return createRequestBuilder(bearer, path).delete(body).build();
  }

  protected Request createAuthorizedDeleteRequest(final String bearer, final String path) {
    return createRequestBuilder(bearer, path).delete().build();
  }

  private Request.Builder createRequestBuilder(final String bearer, final String path) {
    return new Request.Builder().url((zone.toString().concat(path)))
        .addHeader(CONTENT_TYPE, CONTENT_TYPE_APPLICATION_JSON)
        .addHeader(HEADER_AUTHORIZATION, HEADER_BEARER.concat(SPACE).concat(bearer));
  }
}
