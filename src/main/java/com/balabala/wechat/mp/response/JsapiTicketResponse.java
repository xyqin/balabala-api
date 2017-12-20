package com.balabala.wechat.mp.response;

import com.balabala.wechat.mp.WxMpResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by xyqin on 2017/4/8.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class JsapiTicketResponse extends WxMpResponse {

    private String ticket;

    @JsonProperty("expires_in")
    private int expires_in;

}
