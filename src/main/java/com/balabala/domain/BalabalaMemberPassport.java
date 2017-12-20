package com.balabala.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class BalabalaMemberPassport extends AbstractEntity<Long> {

    private Long memberId;

    private BalabalaMemberPassportProvider provider;

    private String providerId;

    private String password;

}
