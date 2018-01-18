package com.barablah.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class BarablahMemberPassport extends AbstractEntity<Long> {

    private Long memberId;

    private MemberPassportProvider provider;

    private String providerId;

    private String password;

}
