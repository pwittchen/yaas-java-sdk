package com.github.pwittchen.yaas.sdk;

import io.reactivex.Flowable;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Authorization interface is used to perform authorization procedure and make authorized HTTP
 * requests to the microservice. It's used by the {@link com.github.pwittchen.yaas.sdk.Client}
 */
public interface Authorization {
  Flowable<String> getAccessToken(final String clientId, final String clientSecret);

  Flowable<Response> get(final String bearer, final String path);

  Flowable<Response> post(final String bearer, final String path, final RequestBody body);

  Flowable<Response> put(final String bearer, final String path, final RequestBody body);

  Flowable<Response> delete(final String bearer, final String path, final RequestBody body);

  Flowable<Response> delete(final String bearer, final String path);
}
