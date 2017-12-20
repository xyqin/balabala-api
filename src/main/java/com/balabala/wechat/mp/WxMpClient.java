package com.balabala.wechat.mp;

import com.balabala.wechat.WechatProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.collections4.MapUtils;

import java.util.Map;

/**
 * Created by xyqin on 2017/4/1.
 */
@Slf4j
public class WxMpClient {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private final static ObjectMapper MAPPER = new ObjectMapper();

    private final OkHttpClient httpClient = new OkHttpClient.Builder().build();

    @Getter
    private WechatProperties properties;

    public WxMpClient(WechatProperties properties) {
        this.properties = properties;
    }

    public <T extends WxMpResponse> T execute(WxMpRequest<T> request) throws Exception {
        // 设置API通用属性
        request.setAppid(properties.getAppId());
        request.setSecret(properties.getAppSecret());

        Request httpRequest = null;
        HttpUrl.Builder urlBuilder = HttpUrl.parse(request.getUrl()).newBuilder();

        // 构造查询参数
        if (MapUtils.isNotEmpty(request.getQuery())) {
            for (Map.Entry<String, String> entry : request.getQuery().entrySet()) {
                urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
            }
        }

        if (HttpMethod.GET.equals(request.getMethod())) {
            // 构造HTTP请求
            httpRequest = new Request.Builder().url(urlBuilder.build()).build();
        } else if (HttpMethod.POST.equals(request.getMethod())) {
            // 构造请求体
            RequestBody body = RequestBody.create(JSON, request.getBody());

            // 构造HTTP请求
            httpRequest = new Request.Builder().url(request.getUrl()).post(body).build();
        }

        Response httpResponse = httpClient.newCall(httpRequest).execute();
        String json = httpResponse.body().string();
        log.debug("wechat:mp:api响应, api={}, body={}", request.getUrl(), json);
        T response = MAPPER.readValue(json, request.getResponseClass());
        return response;
    }

}
