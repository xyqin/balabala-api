package com.barablah.web.response;

import lombok.Data;

import java.util.Date;

@Data
public class HomeworkDto {

    private Long id;

    //班级
    private String className;

    //评语
    private String content;

    //分数(1-5),如果是0,那么就没有评语和积分(作业状态不用额外处理,我直接返回给你的是中文,直接显示即可)
    private int score;

    //积分
    private int points;


    private String name;

    private String teacher;

    private String status;

    private Date closingAt;

    private int finishStatus;




}
