package com.barablah.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
public class BarablahMemberSignup extends AbstractEntity<Long> {

    private Long memberId;

    private Long classId;

    private BigDecimal amount;

    private String payment;

}
