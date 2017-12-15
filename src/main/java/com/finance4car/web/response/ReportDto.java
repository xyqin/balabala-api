package com.finance4car.web.response;

import lombok.Data;

import java.util.Date;

@Data
public class ReportDto {

    private Long id;

    private String name;

    /**
     * 格式为yyyyMM
     */
    private int month;

    private Date startAt;

    private Date endAt;

}
