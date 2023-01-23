/*
 * MIT License
 *
 * Copyright (c) 2023 Silvio Wangler
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.wangler.micronaut.smocker;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.http.client.annotation.Client;
import java.util.List;

@Client(id = "smocker-api")
public interface SmockerClient {

  String DEFAULT_SESSION_NAME = "my-session";

  @Post("/reset")
  void reset();

  @Post("/mocks")
  SmockerResponse addMocks(
      @QueryValue(value = "reset", defaultValue = "false") boolean reset,
      @Body List<SmockerMock> mocks);

  @Post("/mocks")
  SmockerResponse addMocks(
      @QueryValue(value = "reset", defaultValue = "false") boolean reset,
      @QueryValue(value = "session", defaultValue = DEFAULT_SESSION_NAME) String session,
      @Body List<SmockerMock> mocks);

  @Get("/mocks")
  List<SmockerMock> getMocks(
      @QueryValue(value = "session", defaultValue = DEFAULT_SESSION_NAME) String session);

  @Post("/sessions")
  SmockerSession startSession(
      @QueryValue(value = "name", defaultValue = DEFAULT_SESSION_NAME) String session);

  @Get("/sessions")
  List<SmockerSession> getSessions();

  @Get("/version")
  SmockerVersion healthcheck();
}
