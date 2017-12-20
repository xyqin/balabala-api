package com.balabala.wechat.mp.request;

import com.balabala.wechat.mp.HttpMethod;
import com.balabala.wechat.mp.WxMpRequest;
import com.balabala.wechat.mp.response.QrcodeCreateResponse;
import com.google.common.collect.Maps;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xyqin on 2017/4/12.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class QrcodeCreateRequest extends WxMpRequest<QrcodeCreateResponse> {

    private int expireSeconds = 3600;

    private String actionName = "QR_SCENE";

    private int sceneId;

    @Override
    public HttpMethod getMethod() {
        return HttpMethod.POST;
    }

    @Override
    public String getUrl() {
        return "https://api.weixin.qq.com/cgi-bin/qrcode/create";
    }

    @Override
    public Map<String, String> getQuery() {
        HashMap<String, String> query = Maps.newHashMap();
        query.put("access_token", getAccessToken());
        return query;
    }

    @Override
    public String getBody() {
        return "{\"expire_seconds\": " + expireSeconds + ", \"action_name\": \"" + actionName + "\", \"action_info\": {\"scene\": {\"scene_id\": " + sceneId + "}}}";
    }

    @Override
    public Class<QrcodeCreateResponse> getResponseClass() {
        return QrcodeCreateResponse.class;
    }

}
