package com.balabala.wechat.mp.response;

import lombok.Data;

/**
 * Created by xyqin on 2017/4/12.
 */
@Data
public class MpNotifyResponse {

    private String toUserName;

    private String fromUserName;

    private int createTime;

    private String msgType;

    private String event;

    private String eventKey;

    private String ticket;

}
