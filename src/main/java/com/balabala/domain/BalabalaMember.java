package com.balabala.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class BalabalaMember extends AbstractEntity<Long> {

    private Long campusId;

    private String nickname;

    private String avatar;

    private String englishName;

    private BalabalaMemberGender gender;

    private String birthday;

    private String accid;

    private String token;

    private Integer points;

}
