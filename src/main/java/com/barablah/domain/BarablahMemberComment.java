package com.barablah.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class BarablahMemberComment extends AbstractEntity<Long> {

    private Long memberId;

    private Long teacherId;

    private String content;

}