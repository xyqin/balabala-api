package com.barablah.netease;

import lombok.Data;

import java.util.Map;

/**
 * Created by xyqin on 2017/4/1.
 */
@Data
public abstract class NeteaseRequest<T extends NeteaseResponse> {

    private String appKey;

    private String appSecret;

    /**
     * 获取请求方法
     *
     * @return
     */
    public abstract HttpMethod getMethod();

    /**
     * 获取请求URL
     *
     * @return
     */
    public abstract String getUrl();

    /**
     * 获取查询参数,以Map形式返回
     *
     * @return
     */
    public abstract Map<String, String> getQuery();

    /**
     * 获取头信息,以Map形式返回
     *
     * @return
     */
    public abstract Map<String, String> getHeaders();

    /**
     * 获取请求体,以字符串形式返回
     *
     * @return
     */
    public abstract Map<String, String> getBody();

    /**
     * 获取响应类类型
     *
     * @return
     */
    public abstract Class<T> getResponseClass();

}
