package com.barablah.web.enums;

/**
 *
 */
public enum HomeworkStatusEnum {
    待完成("WAITTING"),已完成("FINISHED"),已超时("EXTTIME"),延时完成("DELAY");
    private String value;


    //老师发布作业后：1、到期前学员未做的状态：待完成；2、到期前完成作业了的状态：已完成；3、到期后未完成的状态：已超时；4、到期后完成做业的状态：延时完成；
    private HomeworkStatusEnum(String value) {
        this.value = value;
    }

    public static String getLabel(String value) {
        HomeworkStatusEnum[] enums = HomeworkStatusEnum.values();
        for(HomeworkStatusEnum e:enums) {
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
