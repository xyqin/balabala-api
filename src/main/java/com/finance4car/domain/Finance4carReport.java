package com.finance4car.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class Finance4carReport extends AbstractEntity<Long> {

    private Long memberId;

    private String reportName;

    /**
     * 格式为yyyyMM，0为未汇总数据
     */
    private Integer month;

    private Date startAt;

    private Date endAt;

    private String content;

}
