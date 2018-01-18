package com.barablah.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class BarablahMemberHomeworkItem extends AbstractEntity<Long> {

    private Long memberId;

    private Long homeworkId;

    private Long textbookId;

    private String answer;

}