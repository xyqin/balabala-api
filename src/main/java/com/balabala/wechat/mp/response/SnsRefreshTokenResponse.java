package com.balabala.wechat.mp.response;

import com.balabala.wechat.mp.WxMpResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by xyqin on 2017/4/1.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SnsRefreshTokenResponse extends WxMpResponse {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("expires_in")
    private int expiresIn;

    @JsonProperty("refresh_token")
    private String refreshToken;

    private String openid;

    private String scope;

}
