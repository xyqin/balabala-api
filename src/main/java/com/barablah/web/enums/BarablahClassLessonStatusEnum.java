package com.barablah.web.enums;

/**
 *
 */
public enum BarablahClassLessonStatusEnum {
待开课("WAITING"),开课中("GOING"),已过期("GONE"),已结束("FINISH");
    private String value;

    private BarablahClassLessonStatusEnum(String value) {
        this.value = value;
    }

    public static String getLabel(String value) {
        BarablahClassLessonStatusEnum[] enums = BarablahClassLessonStatusEnum.values();
        for(BarablahClassLessonStatusEnum e:enums) {
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
