package com.github.pwittchen.yaas.sdk;

import io.reactivex.Flowable;
import okhttp3.RequestBody;
import okhttp3.Response;

public interface Authorization {
  Flowable<String> getAccessToken(final String clientId, final String clientSecret);

  Flowable<Response> get(final String bearer, final String path);

  Flowable<Response> post(final String bearer, final String path, final RequestBody body);

  Flowable<Response> put(final String bearer, final String path, final RequestBody body);

  Flowable<Response> delete(final String bearer, final String path, final RequestBody body);

  Flowable<Response> delete(final String bearer, final String path);
}
