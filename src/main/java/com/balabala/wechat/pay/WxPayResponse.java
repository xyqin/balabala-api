package com.balabala.wechat.pay;

import com.google.common.base.CaseFormat;
import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import lombok.Data;
import org.springframework.util.ReflectionUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Objects;

/**
 * Created by xyqin on 2017/3/30.
 */
@Data
public abstract class WxPayResponse {

    /**
     * 返回状态码,SUCCESS/FAIL
     */
    private String returnCode;

    /**
     * 返回信息
     */
    private String returnMsg;

    /* 以下字段在return_code为SUCCESS的时候有返回 */

    /**
     * 公众账号ID
     */
    private String appid;

    /**
     * 商户号
     */
    private String mchId;

    /**
     * 随机字符串
     */
    private String nonceStr;

    /**
     * 签名
     */
    private String sign;

    /**
     * 业务结果
     */
    private String resultCode;

    /**
     * 错误代码
     */
    private String errCode;

    /**
     * 错误代码描述
     */
    private String errCodeDes;

    public boolean isSuccess() {
        return "SUCCESS".equals(returnCode);
    }

    public boolean isResultSuccess() {
        return "SUCCESS".equals(resultCode);
    }

    public void fromXml(String xml) throws Exception {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        Document document = builder.parse(new ByteArrayInputStream(xml.getBytes(Charsets.UTF_8)));
        Element root = document.getDocumentElement();
        NodeList nodes = root.getChildNodes();
        HashMap<String, Method> methodMap = Maps.newHashMap();
        Method[] methods = ReflectionUtils.getAllDeclaredMethods(this.getClass());

        for (Method method : methods) {
            methodMap.putIfAbsent(method.getName(), method);
        }

        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeType() == 1) {
                String setterName = "set" + CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, node.getNodeName());
                Method setter = methodMap.get(setterName);

                if (Objects.nonNull(setter)) {
                    if (setter.getParameterTypes()[0] == int.class) {
                        setter.invoke(this, Integer.valueOf(node.getTextContent()));
                    } else {
                        setter.invoke(this, node.getTextContent());
                    }
                }
            }
        }
    }

}
