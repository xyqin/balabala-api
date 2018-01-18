package com.barablah.wechat.pay.request;

import com.barablah.wechat.pay.WxPayRequest;
import com.barablah.wechat.pay.response.UnifiedOrderResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by xyqin on 2017/3/31.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UnifiedOrderRequest extends WxPayRequest<UnifiedOrderResponse> {

    private String deviceInfo;

    private String body;

    private String detail;

    private String attach;

    private String outTradeNo;

    private String feeType;

    private int totalFee;

    private String spbillCreateIp;

    private String timeStart;

    private String timeExpire;

    private String goodsTag;

    private String notifyUrl;

    private String tradeType;

    private String productId;

    private String limitPay;

    private String openid;

    @Override
    public String getUrl() {
        return "https://api.mch.weixin.qq.com/pay/unifiedorder";
    }

    @Override
    public Class<UnifiedOrderResponse> getResponseClass() {
        return UnifiedOrderResponse.class;
    }

}
