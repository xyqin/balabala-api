package com.barablah.wechat.mp.request;

import com.barablah.wechat.mp.HttpMethod;
import com.barablah.wechat.mp.WxMpRequest;
import com.barablah.wechat.mp.response.SnsTokenResponse;
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
public class SnsTokenRequest extends WxMpRequest<SnsTokenResponse> {

    private String code;

    private String grantType = "authorization_code";

    @Override
    public HttpMethod getMethod() {
        return HttpMethod.GET;
    }

    @Override
    public String getUrl() {
        return "https://api.weixin.qq.com/sns/oauth2/access_token";
    }

    @Override
    public Map<String, String> getQuery() {
        HashMap<String, String> query = Maps.newHashMap();
        query.put("appid", getAppid());
        query.put("secret", getSecret());
        query.put("code", code);
        query.put("grant_type", grantType);
        return query;
    }

    @Override
    public String getBody() {
        return null;
    }

    @Override
    public Class<SnsTokenResponse> getResponseClass() {
        return SnsTokenResponse.class;
    }

}
