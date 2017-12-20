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
public class AccessTokenResponse extends WxMpResponse {

    /**
     * 获取到的凭证
     */
    @JsonProperty("access_token")
    private String accessToken;

    /**
     * 凭证有效时间,单位:秒
     */
    @JsonProperty("expires_in")
    private int expiresIn;

}
