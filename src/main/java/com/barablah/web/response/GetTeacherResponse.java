package com.barablah.web.response;

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


    private Long majorId;

    private String comeFrom;

    private Long comeFromId;



}
