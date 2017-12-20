package com.balabala.wechat.mp.response;

import com.balabala.wechat.mp.WxMpResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Created by xyqin on 2017/4/1.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SnsUserInfoResponse extends WxMpResponse {

    private String openid;

    private String nickname;

    private int sex;

    private String language;

    private String country;

    private String province;

    private String city;

    private String headimgurl;

    private List<String> privilege;

    private String unionid;

}
