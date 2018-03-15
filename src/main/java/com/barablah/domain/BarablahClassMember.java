package com.barablah.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class BarablahClassMember extends AbstractEntity<Long> {

    private Long classId;

    private Long memberId;

    private String status;

}
