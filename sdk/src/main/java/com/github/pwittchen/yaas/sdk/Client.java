package com.github.pwittchen.yaas.sdk;

import io.reactivex.Flowable;
import okhttp3.RequestBody;
import okhttp3.Response;

public interface Client {
  Flowable<Response> get(final String path);

  Flowable<Response> post(final String path, final RequestBody body);

  Flowable<Response> put(final String path, final RequestBody body);

  Flowable<Response> delete(final String path, final RequestBody body);

  Flowable<Response> delete(final String path);
}
