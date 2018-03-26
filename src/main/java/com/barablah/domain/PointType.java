package com.barablah.domain;

public enum PointType {

    奖杯("TROPHY"),鼓掌("CLAPPING"),开心("SMILING"),作业("HOMEWORK"),班级表现("CLASSPRISE");

    private String _value;

    private PointType(String value) {
        this._value=value;
    }

    public String value() {
        return _value;
    }
}
