package com.barablah.web.request;

import lombok.Data;

@Data
public class UpdateMemberInfoRequest {

    private Long campusId;

    private String nickname;

    private String avatar;

    private String englishName;

    private String gender;

}
