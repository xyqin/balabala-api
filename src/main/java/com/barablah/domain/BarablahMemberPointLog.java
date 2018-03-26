package com.barablah.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class BarablahMemberPointLog extends AbstractEntity<Long> {

    private Long memberId;

    private Integer points;

    private String type;

    private Long objectId;

    /**
     *
     * {"viewconfig":{"optype":"3","formid":"1"},"name":"来源类型","fieldType":"Enum","visible":true,"queryType":2,"displayOrder":0,"length":0,"types":[{"label":"线上课","value":"ONLINELESSON"},{"label":"班级表现","value":"CLASSES"},{"label":"作业","value":"HOMEWORK"}],"valid":true}
     * @generated 2018年3月27日 04:43:53
     */
    private String objectType;
}