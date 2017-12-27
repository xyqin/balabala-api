package com.balabala.web.response;

import lombok.Data;

@Data
public class GetTeacherResponse {

    private Long id;

    private String campus;

    private String username;

    private String avatar;

    private String fullName;

    private String phoneNumber;

    private String major;

    private String comeFrom;

}
