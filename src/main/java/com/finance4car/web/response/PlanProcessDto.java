package com.finance4car.web.response;

import lombok.Data;

import java.util.Date;

@Data
public class PlanProcessDto {

    private Long id;

    private String description;

    private String grossIncrease;

    private String feedback;

    private String improvement;

    private int week;

    private Date startAt;

    private Date endAt;

}
