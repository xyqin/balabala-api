package com.balabala.wechat.pay.response;

import com.balabala.wechat.pay.WxPayResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by xyqin on 2017/3/31.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class NotifyResponse extends WxPayResponse {

    // 以下字段在return_code为SUCCESS的时候有返回

    private String deviceInfo;

    private String openid;

    private String isSubscribe;

    private String tradeType;

    private String bankType;

    private int totalFee;

    private int settlementTotalFee;

    private String feeType;

    private int cashFee;

    private String cashFeeType;

    private String transactionId;

    private String outTradeNo;

    private String attach;

    private String timeEnd;

}
