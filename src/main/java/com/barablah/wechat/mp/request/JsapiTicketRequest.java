package com.barablah.wechat.mp.request;

import com.barablah.wechat.mp.HttpMethod;
import com.barablah.wechat.mp.WxMpRequest;
import com.barablah.wechat.mp.response.JsapiTicketResponse;
import com.google.common.collect.Maps;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xyqin on 2017/4/8.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class JsapiTicketRequest extends WxMpRequest<JsapiTicketResponse> {

    private String accessToken;

    private String type = "jsapi";

    @Override
    public HttpMethod getMethod() {
        return HttpMethod.GET;
    }

    @Override
    public String getUrl() {
        return "https://api.weixin.qq.com/cgi-bin/ticket/getticket";
    }

    @Override
    public Map<String, String> getQuery() {
        HashMap<String, String> query = Maps.newHashMap();
        query.put("access_token", accessToken);
        query.put("type", type);
        return query;
    }

    @Override
    public String getBody() {
        return null;
    }

    @Override
    public Class<JsapiTicketResponse> getResponseClass() {
        return JsapiTicketResponse.class;
    }
}
