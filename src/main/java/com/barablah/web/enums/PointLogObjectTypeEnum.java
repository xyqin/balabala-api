package com.barablah.web.enums;

/**
 *
 */
public enum PointLogObjectTypeEnum {
线上课("ONLINELESSON"),班级表现("CLASSES"),作业("HOMEWORK");
    private String value;

    private PointLogObjectTypeEnum(String value) {
        this.value = value;
    }

    public static String getLabel(String value) {
        PointLogObjectTypeEnum[] enums = PointLogObjectTypeEnum.values();
        for(PointLogObjectTypeEnum e:enums) {
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
