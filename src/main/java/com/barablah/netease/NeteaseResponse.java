package com.barablah.netease;

import lombok.Data;

/**
 * Created by xyqin on 2017/4/1.
 */
@Data
public abstract class NeteaseResponse {

    private int code;

    private String desc;

    private String msg;

    private Object obj;

    public boolean isSuccess() {
        return code == 200;
    }

}
