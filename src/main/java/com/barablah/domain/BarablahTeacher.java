package com.barablah.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class BarablahTeacher extends AbstractEntity<Long> {

    private Long campusId;

    private String username;

    private String password;

    private String avatar;

    private String fullName;

    private String phoneNumber;

    private Long major;

    private Long comeFrom;

    private String accid;

    private String token;

    private TeacherStatus status;

}
