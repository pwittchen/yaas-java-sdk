package com.github.pwittchen.yaas.sdk;

/**
 * Represents response from the YaaS performed during Authorization procedure.
 */
public class YaaSAuthorizationResponse {

  public String tokenType;
  public String accessToken; // AKA Bearer
  public int expiresIn;
  public String scope;
}
