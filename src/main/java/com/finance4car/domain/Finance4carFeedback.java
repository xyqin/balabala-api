package com.finance4car.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class Finance4carFeedback extends AbstractEntity<Long> {

    private String familyName;

    private String givenName;

    private String email;

    private String job;

    private String department;

    private String company;

    private String phoneNumber;

    private String country;

    private String city;

    private String information;

}
