package com.balabala.wechat.pay;

import com.balabala.Utils;
import com.balabala.wechat.WechatProperties;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

/**
 * Created by xyqin on 2017/3/30.
 */
@Slf4j
public class WxPayClient {

    public static final MediaType XML = MediaType.parse("application/xml; charset=utf-8");

    private final OkHttpClient httpClient = new OkHttpClient.Builder().build();

    private WechatProperties properties;

    public WxPayClient(WechatProperties properties) {
        this.properties = properties;
    }

    public <T extends WxPayResponse> T execute(WxPayRequest<T> request) throws Exception {
        // 设置API通用属性
        request.setAppid(properties.getAppId());
        request.setMchId(properties.getMerchantId());
        request.setSignType("MD5");
        request.setNonceStr(Utils.randomAlphanumeric(32));
        request.signWithKey(properties.getApiKey());

        // 构造HTTP请求
        RequestBody body = RequestBody.create(XML, request.toXml());
        Request httpRequest = new Request.Builder()
                .url(request.getUrl())
                .post(body)
                .build();

        Response httpResponse = httpClient.newCall(httpRequest).execute();
        String xml = httpResponse.body().string();
        log.debug("wechat:pay:api响应, api={}, body={}", request.getUrl(), xml);
        T response = request.getResponseClass().newInstance();
        response.fromXml(xml);
        return response;
    }

}
