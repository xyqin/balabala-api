package com.barablah.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class BarablahMember extends AbstractEntity<Long> {

    private Long campusId;

    private String nickname;

    private String avatar;

    private String englishName;

    private MemberGender gender;

    private String birthday;

    private MemberStatus status;

    private String accid;

    private String token;

    private Integer points;

}
