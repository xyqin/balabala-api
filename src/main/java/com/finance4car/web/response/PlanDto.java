package com.finance4car.web.response;

import lombok.Data;

import java.util.Date;

@Data
public class PlanDto {

    private Long id;

    private String name;

    private boolean processModified;

    private boolean actionModified;

    private Date createdAt;

}
