package com.finance4car.web.request;

import lombok.Data;

import java.util.Date;

@Data
public class UpdateMemberInfoRequest {

    private Long regionId;

    private String name;

    private String dealer;

    private String group;

    private String brand;

    private String postalAddress;

    private int hallArea;

    private Date openAt;

}
