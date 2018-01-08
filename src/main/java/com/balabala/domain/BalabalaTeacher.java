package com.balabala.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class BalabalaTeacher extends AbstractEntity<Long> {

    private Long campusId;

    private String username;

    private String password;

    private String avatar;

    private String fullName;

    private String phoneNumber;

    private String major;

    private String comeFrom;

    private String accid;

    private String token;

    private TeacherStatus status;

}
