package com.balabala.wechat.pay.response;

import com.balabala.wechat.pay.WxPayResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by xyqin on 2017/3/31.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UnifiedOrderResponse extends WxPayResponse {

    private String deviceInfo;

    private String tradeType;

    private String prepayId;

    private String codeUrl;

}
