package com.barablah.web.enums;

/**
 *
 */
public enum BarablahMemberStatusEnum {
启用("ENABLED"),禁用("DISABLED");
    private String value;

    private BarablahMemberStatusEnum(String value) {
        this.value = value;
    }

    public static String getLabel(String value) {
        BarablahMemberStatusEnum[] enums = BarablahMemberStatusEnum.values();
        for(BarablahMemberStatusEnum e:enums) {
            if (e.getValue().equals(value)) {
                return  e.name();
            }
        }
        return "";
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
