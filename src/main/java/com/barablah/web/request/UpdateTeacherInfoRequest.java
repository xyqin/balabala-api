package com.barablah.web.request;

import lombok.Data;

@Data
public class UpdateTeacherInfoRequest {

    private String avatar;

    private String fullName;

    private Long major;

    private Long comeFrom;

}
