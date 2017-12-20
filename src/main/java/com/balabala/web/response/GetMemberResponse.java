package com.balabala.web.response;

import lombok.Data;

import java.util.Date;

/**
 * Created by xyqin on 2017/4/7.
 */
@Data
public class GetMemberResponse {

    private Long id;

    private Long regionId;

    private String region;

    private String level;

    private String name;

    private String phoneNumber;

    private String email;

    private String dealer;

    private String group;

    private String brand;

    private String postalAddress;

    private int hallArea;

    private Date openAt;

}
