package com.barablah.web.enums;

/**
 *
 */
public enum PointLogTypeEnum {
奖杯("TROPHY"),鼓掌("CLAPPING"),开心("SMILING");
    private String value;

    private PointLogTypeEnum(String value) {
        this.value = value;
    }

    public static String getLabel(String value) {
        PointLogTypeEnum[] enums = PointLogTypeEnum.values();
        for(PointLogTypeEnum e:enums) {
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
