package com.barablah.netease.request;

import com.barablah.Utils;
import com.barablah.netease.CheckSumBuilder;
import com.barablah.netease.HttpMethod;
import com.barablah.netease.NeteaseRequest;
import com.barablah.netease.response.ImUserCreateResponse;
import com.google.common.collect.Maps;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xyqin on 2017/4/1.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ImUserCreateRequest extends NeteaseRequest<ImUserCreateResponse> {

    private String accid;

    @Override
    public HttpMethod getMethod() {
        return HttpMethod.POST;
    }

    @Override
    public String getUrl() {
        return "https://api.netease.im/nimserver/user/create.action";
    }

    @Override
    public Map<String, String> getQuery() {
        return null;
    }

    @Override
    public Map<String, String> getHeaders() {
        String nonce = Utils.randomNumeric(32);
        String curTime = String.valueOf((new Date()).getTime() / 1000L);
        String checkSum = CheckSumBuilder.getCheckSum(getAppSecret(), nonce, curTime);

        HashMap<String, String> headers = Maps.newHashMap();
        headers.put("AppKey", getAppKey());
        headers.put("Nonce", nonce);
        headers.put("CurTime", curTime);
        headers.put("CheckSum", checkSum);
        return headers;
    }

    @Override
    public Map<String, String> getBody() {
        HashMap<String, String> body = Maps.newHashMap();
        body.put("accid", getAccid());
        return body;
    }

    @Override
    public Class<ImUserCreateResponse> getResponseClass() {
        return ImUserCreateResponse.class;
    }

}
