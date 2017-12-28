package com.balabala.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class BalabalaMemberHomework extends AbstractEntity<Long> {

    private Long memberId;

    private Long teacherId;

    private String homeworkName;

    private Date closingAt;

}