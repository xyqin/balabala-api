package com.barablah.web.enums;

/**
 *
 */
public enum BarablahMemberGenderEnum {
男("MALE"),女("FEMALE");
    private String value;

    private BarablahMemberGenderEnum(String value) {
        this.value = value;
    }

    public static String getLabel(String value) {
        BarablahMemberGenderEnum[] enums = BarablahMemberGenderEnum.values();
        for(BarablahMemberGenderEnum e:enums) {
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
