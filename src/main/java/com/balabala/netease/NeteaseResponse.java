package com.balabala.netease;

import lombok.Data;

/**
 * Created by xyqin on 2017/4/1.
 */
@Data
public abstract class NeteaseResponse {

    private int code;

    public boolean isSuccess() {
        return code == 200;
    }

}