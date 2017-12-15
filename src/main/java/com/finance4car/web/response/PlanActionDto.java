package com.finance4car.web.response;

import lombok.Data;

import java.util.Date;

@Data
public class PlanActionDto {

    private Long id;

    private String name;

    private String description;

    private String output;

    private String communication;

    private String resource;

    private String inCharge;

    private Date implementAt;

    private String startNote;

    private String improvement;

}
