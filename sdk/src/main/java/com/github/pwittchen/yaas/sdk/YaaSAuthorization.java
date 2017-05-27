package com.github.pwittchen.yaas.sdk;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import java.io.IOException;
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
  private final OkHttpClient client;
  private final Gson gson;

  public YaaSAuthorization(final Zone zone) {
    this.zone = zone;
    this.client = new OkHttpClient();
    this.gson =
        new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();
  }

  @Override
  public Flowable<String> getAccessToken(final String clientId, final String clientSecret) {
    final FormBody requestBody = createAccessTokenRequestBody(clientId, clientSecret);
    final Request request = createAccessTokenRequest(requestBody);

    return Flowable.create(emitter -> client.newCall(request).enqueue(new Callback() {
      @Override public void onFailure(Call call, IOException e) {
        emitter.onError(e);
        emitter.onComplete();
      }

      @Override public void onResponse(final Call call, final Response response)
          throws IOException {
        final ResponseBody body = response.body();
        if (body == null) {
          emitter.onError(new YaaSException("ResponseBody == null"));
        } else {
          String accessToken = retrieveAccessToken(body);
          if (accessToken == null || accessToken.isEmpty()) {
            emitter.onError(new YaaSException("Access Token is empty"));
          } else {
            emitter.onNext(accessToken);
          }
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

  protected String retrieveAccessToken(final ResponseBody responseBody) throws IOException {
    String body = responseBody.string();
    YaaSAuthorizationResponse authResponse = gson.fromJson(body, YaaSAuthorizationResponse.class);
    return authResponse.accessToken;
  }

  @Override public Flowable<Response> get(final String bearer, final String path) {
    return request(createAuthorizedGetRequest(bearer, path));
  }

  @Override
  public Flowable<Response> post(final String bearer, final String path, final RequestBody body) {
    return request(createAuthorizedPostRequest(bearer, path, body));
  }

  @Override public Flowable<Response> put(String bearer, String path, RequestBody body) {
    return request(createAuthorizedPutRequest(bearer, path, body));
  }

  @Override public Flowable<Response> delete(String bearer, String path, RequestBody body) {
    return request(createAuthorizedDeleteRequest(bearer, path, body));
  }

  @Override public Flowable<Response> delete(String bearer, String path) {
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

  private Request.Builder createRequestBuilder(String bearer, String path) {
    return new Request.Builder().url((zone.toString().concat(path)))
        .addHeader(CONTENT_TYPE, CONTENT_TYPE_APPLICATION_JSON)
        .addHeader(HEADER_AUTHORIZATION, HEADER_BEARER.concat(SPACE).concat(bearer));
  }
}
