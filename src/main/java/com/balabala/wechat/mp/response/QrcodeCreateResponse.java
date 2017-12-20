package com.balabala.wechat.mp.response;

import com.balabala.wechat.mp.WxMpResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by xyqin on 2017/4/12.
 */
public class QrcodeCreateResponse extends WxMpResponse {

    private String ticket;

    @JsonProperty("expire_seconds")
    private int expireSeconds;

    private String url;

}
