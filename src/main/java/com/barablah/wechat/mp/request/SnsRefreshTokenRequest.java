package com.barablah.wechat.mp.request;

import com.barablah.wechat.mp.HttpMethod;
import com.barablah.wechat.mp.WxMpRequest;
import com.barablah.wechat.mp.response.SnsRefreshTokenResponse;
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
public class SnsRefreshTokenRequest extends WxMpRequest<SnsRefreshTokenResponse> {

    private String grantType = "refresh_token";

    private String refreshToken;

    @Override
    public HttpMethod getMethod() {
        return HttpMethod.GET;
    }

    @Override
    public String getUrl() {
        return "https://api.weixin.qq.com/sns/oauth2/refresh_token";
    }

    @Override
    public Map<String, String> getQuery() {
        HashMap<String, String> query = Maps.newHashMap();
        query.put("appid", getAppid());
        query.put("grant_type", grantType);
        query.put("refresh_token", refreshToken);
        return query;
    }

    @Override
    public String getBody() {
        return null;
    }

    @Override
    public Class<SnsRefreshTokenResponse> getResponseClass() {
        return SnsRefreshTokenResponse.class;
    }

}
