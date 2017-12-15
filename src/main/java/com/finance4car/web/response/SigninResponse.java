package com.finance4car.web.response;

import lombok.Data;

/**
 * Created by xyqin on 2017/6/2.
 */
@Data
public class SigninResponse {

    private Long memberId;

    private String memberName;

    private String phoneNumber;

    private String email;

}
