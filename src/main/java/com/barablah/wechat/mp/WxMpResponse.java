package com.barablah.wechat.mp;

import lombok.Data;

/**
 * Created by xyqin on 2017/4/1.
 */
@Data
public abstract class WxMpResponse {

    private int errcode;

    private String errmsg;

    public boolean isSuccess() {
        return errcode == 0;
    }

}
