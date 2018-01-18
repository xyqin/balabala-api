package com.barablah.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class BarablahPositionContent extends AbstractEntity<Long> {

    private Long positionId;

    private String contentName;

    private String image;

    private String link;

    private Date startAt;

    private Date endAt;

    private Integer position;

}