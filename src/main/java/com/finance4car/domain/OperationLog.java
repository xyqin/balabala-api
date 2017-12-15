package com.finance4car.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class OperationLog extends AbstractEntity<Long> {

    private Long memberId;

    private Long adminId;

    private OperationType operation;

    private Long referenceId;

    private String referenceType;

    private String value;

}
