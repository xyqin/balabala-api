package com.barablah.web;

import lombok.Data;

/**
 * Created by ling on 2018/3/27.
 */
@Data
public class CorrectHomeworkRequest {
    //作业
    private Long homeworkId;

    //学员
    private Long memberId;

    //作业评论内容
    private String content;

    //分数
    private int scoreLevel;

    //积分
    private int point;
}
