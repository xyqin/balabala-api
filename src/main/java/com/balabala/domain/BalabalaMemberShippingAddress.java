package com.balabala.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class BalabalaMemberShippingAddress extends AbstractEntity<Long> {

    private Long memberId;

    private String fullName;

    private String region;

    private String regionPath;

    private String streetAddress;

    private String phoneNumber;

    private Byte prior;

}
