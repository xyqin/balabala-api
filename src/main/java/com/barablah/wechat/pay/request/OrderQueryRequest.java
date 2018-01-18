package com.barablah.wechat.pay.request;

import com.barablah.wechat.pay.WxPayRequest;
import com.barablah.wechat.pay.response.OrderQueryResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by xyqin on 2017/3/31.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class OrderQueryRequest extends WxPayRequest<OrderQueryResponse> {

    private String transactionId;

    private String outTradeNo;

    @Override
    public String getUrl() {
        return "https://api.mch.weixin.qq.com/pay/orderquery";
    }

    @Override
    public Class<OrderQueryResponse> getResponseClass() {
        return OrderQueryResponse.class;
    }

}
