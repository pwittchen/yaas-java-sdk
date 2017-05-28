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
