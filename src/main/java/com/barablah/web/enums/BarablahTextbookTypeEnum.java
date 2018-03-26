package com.barablah.web.enums;

/**
 *
 */
public enum BarablahTextbookTypeEnum {
选择题("CHOICE"),填空题("FILLIN"),听写题("LISTEN"),看图造句("SENTENCE"),图词对应("CONNECT"),认词拼读("WORD"),
    看图读词("PICTURE"),阅读文章("ARTICLE");
    private String value;

    private BarablahTextbookTypeEnum(String value) {
        this.value = value;
    }

    public static String getLabel(String value) {
        BarablahTextbookTypeEnum[] enums = BarablahTextbookTypeEnum.values();
        for(BarablahTextbookTypeEnum e:enums) {
            if (e.getValue().equals(value)) {
                return  e.name();
            }
        }
        return "";
    }

    public static boolean isNeed(String type) {
        if (type.toUpperCase().equals("WORD") || type.toUpperCase().equals("PICTURE")
                || type.toUpperCase().equals("ARTICLE")) {
            return false;
        }
        return true;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
