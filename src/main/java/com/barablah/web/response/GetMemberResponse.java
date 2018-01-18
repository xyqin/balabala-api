package com.barablah.web.response;

import lombok.Data;

/**
 * Created by xyqin on 2017/4/7.
 */
@Data
public class GetMemberResponse {

    private Long id;

    private String nickname;

    private String avatar;

    private String englishName;

    private String gender;

    private String campus;

    private int points;

    private boolean wechatBound;

}
