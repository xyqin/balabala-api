package com.barablah.web.enums;

/**
 *
 */
public enum BarablahClassStatusEnum {
审核中("IN_REVIEW"),审核被拒("REJECTED"),待开课("WAITTING"), 已开课("ONGOING"),已结束("FINISHED");
    private String value;

    private BarablahClassStatusEnum(String value) {
        this.value = value;
    }

    public static String getLabel(String value) {
        BarablahClassStatusEnum[] enums = BarablahClassStatusEnum.values();
        for(BarablahClassStatusEnum e:enums) {
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
