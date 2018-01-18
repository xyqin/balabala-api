package com.barablah.netease.response;

import com.barablah.netease.NeteaseResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by xyqin on 2017/4/1.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ImUserCreateResponse extends NeteaseResponse {

    private Info info;

    @Data
    public static class Info {

        private String token;

        private String accid;

        private String name;

    }

}
