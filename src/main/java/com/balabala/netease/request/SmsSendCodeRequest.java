package com.balabala.netease.request;

import com.balabala.Utils;
import com.balabala.netease.CheckSumBuilder;
import com.balabala.netease.HttpMethod;
import com.balabala.netease.NeteaseRequest;
import com.balabala.netease.response.SmsSendCodeResponse;
import com.google.common.collect.Maps;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = false)
public class SmsSendCodeRequest extends NeteaseRequest<SmsSendCodeResponse> {

    private String mobile;

    @Override
    public HttpMethod getMethod() {
        return HttpMethod.POST;
    }

    @Override
    public String getUrl() {
        return "https://api.netease.im/sms/sendcode.action";
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
        body.put("mobile", getMobile());
        return body;
    }

    @Override
    public Class<SmsSendCodeResponse> getResponseClass() {
        return SmsSendCodeResponse.class;
    }

}
