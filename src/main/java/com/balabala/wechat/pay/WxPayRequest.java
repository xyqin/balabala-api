package com.balabala.wechat.pay;

import com.google.common.base.CaseFormat;
import com.google.common.collect.Maps;
import lombok.Data;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * Created by xyqin on 2017/3/30.
 */
@Data
public abstract class WxPayRequest<T extends WxPayResponse> {

    /**
     * 公众账号ID
     */
    private String appid;

    /**
     * 商户号
     */
    private String mchId;

    /**
     * 随机字符串,不长于32位
     */
    private String nonceStr;

    /**
     * 签名
     */
    private String sign;

    /**
     * 签名类型,目前支持HMAC-SHA256和MD5,默认为MD5
     */
    private String signType;

    /**
     * 获取请求URL
     *
     * @return
     */
    public abstract String getUrl();

    /**
     * 获取响应类类型
     *
     * @return
     */
    public abstract Class<T> getResponseClass();

    public String toXml() throws InvocationTargetException, IllegalAccessException {
        TreeMap<String, Object> data = Maps.newTreeMap();
        Method[] methods = ReflectionUtils.getAllDeclaredMethods(this.getClass());

        for (Method m : methods) {
            if (m.getName().startsWith("get") && !"getClass".equals(m.getName())
                    && !"getUrl".equals(m.getName()) && !"getResponseClass".equals(m.getName())) {
                Object value = m.invoke(this);

                if (Objects.nonNull(value) && StringUtils.isNotBlank(value.toString())) {
                    data.put(CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, m.getName().substring(3)), value);
                }
            }
        }

        StringBuilder xml = new StringBuilder("<xml>");

        for (Map.Entry entry : data.entrySet()) {
            xml.append("<").append(entry.getKey()).append(">").append(entry.getValue()).append("</").append(entry.getKey()).append(">");
        }

        xml.append("</xml>");
        return xml.toString();
    }

    public void signWithKey(String key) throws InvocationTargetException, IllegalAccessException {
        TreeMap<String, Object> data = Maps.newTreeMap();
        Method[] methods = ReflectionUtils.getAllDeclaredMethods(this.getClass());

        for (Method m : methods) {
            if (m.getName().startsWith("get") && !"getClass".equals(m.getName())
                    && !"getUrl".equals(m.getName()) && !"getResponseClass".equals(m.getName())) {
                Object value = m.invoke(this);

                if (Objects.nonNull(value) && StringUtils.isNotBlank(value.toString())) {
                    data.put(CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, m.getName().substring(3)), value);
                }
            }
        }

        StringBuilder query = new StringBuilder();

        for (Map.Entry entry : data.entrySet()) {
            query.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }

        query.append("key=").append(key);
        this.sign = DigestUtils.md5Hex(query.toString()).toUpperCase();
    }

}
