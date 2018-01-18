package com.barablah.web.request;

import lombok.Data;

@Data
public class UpdateTeacherInfoRequest {

    private String avatar;

    private String fullName;

    private String major;

    private String comeFrom;

}
