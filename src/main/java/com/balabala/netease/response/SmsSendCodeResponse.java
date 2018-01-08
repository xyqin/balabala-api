package com.balabala.netease.response;

import com.balabala.netease.NeteaseResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class SmsSendCodeResponse extends NeteaseResponse {

    private String msg;

    private String obj;

}
