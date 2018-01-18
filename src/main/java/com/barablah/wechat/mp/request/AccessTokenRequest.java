package com.barablah.wechat.mp.request;

import com.barablah.wechat.mp.HttpMethod;
import com.barablah.wechat.mp.WxMpRequest;
import com.barablah.wechat.mp.response.AccessTokenResponse;
import com.google.common.collect.Maps;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xyqin on 2017/4/1.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AccessTokenRequest extends WxMpRequest<AccessTokenResponse> {

    private String grantType = "client_credential";

    @Override
    public HttpMethod getMethod() {
        return HttpMethod.GET;
    }

    @Override
    public String getUrl() {
        return "https://api.weixin.qq.com/cgi-bin/token";
    }

    @Override
    public Map<String, String> getQuery() {
        HashMap<String, String> query = Maps.newHashMap();
        query.put("grant_type", grantType);
        query.put("appid", getAppid());
        query.put("secret", getSecret());
        return query;
    }

    @Override
    public String getBody() {
        return null;
    }

    @Override
    public Class<AccessTokenResponse> getResponseClass() {
        return AccessTokenResponse.class;
    }

}
