package com.balabala.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class BalabalaClassMember extends AbstractEntity<Long> {

    private Long classId;

    private Long memberId;

}
