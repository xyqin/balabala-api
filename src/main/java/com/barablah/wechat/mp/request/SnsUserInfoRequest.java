package com.barablah.wechat.mp.request;

import com.barablah.wechat.mp.HttpMethod;
import com.barablah.wechat.mp.WxMpRequest;
import com.barablah.wechat.mp.response.SnsUserInfoResponse;
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
public class SnsUserInfoRequest extends WxMpRequest<SnsUserInfoResponse> {

    private String openid;

    private String lang = "zh_CN";

    @Override
    public HttpMethod getMethod() {
        return HttpMethod.GET;
    }

    @Override
    public String getUrl() {
        return "https://api.weixin.qq.com/sns/userinfo";
    }

    @Override
    public Map<String, String> getQuery() {
        HashMap<String, String> query = Maps.newHashMap();
        query.put("access_token", getAccessToken());
        query.put("openid", openid);
        query.put("lang", lang);
        return query;
    }

    @Override
    public String getBody() {
        return null;
    }

    @Override
    public Class<SnsUserInfoResponse> getResponseClass() {
        return SnsUserInfoResponse.class;
    }

}
