package com.barablah.netease.request;

import com.barablah.Utils;
import com.barablah.netease.CheckSumBuilder;
import com.barablah.netease.HttpMethod;
import com.barablah.netease.NeteaseRequest;
import com.barablah.netease.response.SmsVerifyCodeResponse;
import com.google.common.collect.Maps;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = false)
public class SmsVerifyCodeRequest extends NeteaseRequest<SmsVerifyCodeResponse> {

    private String mobile;

    private String code;

    @Override
    public HttpMethod getMethod() {
        return HttpMethod.POST;
    }

    @Override
    public String getUrl() {
        return "https://api.netease.im/sms/verifycode.action";
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
        body.put("code", getCode());
        return body;
    }

    @Override
    public Class<SmsVerifyCodeResponse> getResponseClass() {
        return SmsVerifyCodeResponse.class;
    }

}
