package com.barablah.netease;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.collections4.MapUtils;

import java.io.IOException;
import java.util.Map;

/**
 * Created by xyqin on 2017/4/1.
 */
@Slf4j
public class NeteaseClient {

    private final static ObjectMapper MAPPER = new ObjectMapper();

    private final OkHttpClient httpClient = new OkHttpClient.Builder().build();

    private NeteaseProperties properties;

    public NeteaseClient(NeteaseProperties properties) {
        this.properties = properties;
    }

    public <T extends NeteaseResponse> T execute(NeteaseRequest<T> request) throws IOException {
        // 设置API通用属性
        request.setAppKey(properties.getAppKey());
        request.setAppSecret(properties.getAppSecret());

        Request httpRequest = null;
        HttpUrl.Builder urlBuilder = HttpUrl.parse(request.getUrl()).newBuilder();
        Headers.Builder headerBuilder = new Headers.Builder();

        // 构造查询参数
        if (MapUtils.isNotEmpty(request.getQuery())) {
            for (Map.Entry<String, String> entry : request.getQuery().entrySet()) {
                urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
            }
        }

        if (MapUtils.isNotEmpty(request.getHeaders())) {
            for (Map.Entry<String, String> entry : request.getHeaders().entrySet()) {
                headerBuilder.add(entry.getKey(), entry.getValue());
            }
        }

        if (HttpMethod.GET.equals(request.getMethod())) {
            // 构造HTTP请求
            httpRequest = new Request.Builder()
                    .url(urlBuilder.build())
                    .headers(headerBuilder.build())
                    .build();
        } else if (HttpMethod.POST.equals(request.getMethod())) {
            // 构造请求体
            FormBody.Builder bodyBuilder = new FormBody.Builder();

            if (MapUtils.isNotEmpty(request.getBody())) {
                for (Map.Entry<String, String> entry : request.getBody().entrySet()) {
                    bodyBuilder.addEncoded(entry.getKey(), entry.getValue());
                }
            }

            // 构造HTTP请求
            httpRequest = new Request.Builder()
                    .url(request.getUrl())
                    .headers(headerBuilder.build())
                    .post(bodyBuilder.build())
                    .build();
        }

        Response httpResponse = httpClient.newCall(httpRequest).execute();
        String json = httpResponse.body().string();
        log.debug("netease:api响应, api={}, body={}", request.getUrl(), json);
        T response = MAPPER.readValue(json, request.getResponseClass());
        return response;
    }

}
