package org.report.reportstatusmesssale.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "lark")
public class LarkBaseProperties {

  private String appId;
  private String appSecret;
  private String appToken;    // obj_token của Bitable
  private String baseUrl;     // https://open.larksuite.com

  // user_access_token: user nhập trên UI, mình set runtime
  private volatile String userAccessToken;

  public boolean hasUserAccessToken() {
    return userAccessToken != null && !userAccessToken.isBlank();
  }
}
