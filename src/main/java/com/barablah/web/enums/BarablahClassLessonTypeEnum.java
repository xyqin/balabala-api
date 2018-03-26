package com.barablah.web.enums;

/**
 *
 */
public enum BarablahClassLessonTypeEnum {
线上("ONLINE"),线下("OFFLINE");
    private String value;

    private BarablahClassLessonTypeEnum(String value) {
        this.value = value;
    }

    public static String getLabel(String value) {
        BarablahClassLessonTypeEnum[] enums = BarablahClassLessonTypeEnum.values();
        for(BarablahClassLessonTypeEnum e:enums) {
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
