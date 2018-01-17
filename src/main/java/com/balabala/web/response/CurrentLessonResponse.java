package com.balabala.web.response;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CurrentLessonResponse {

    private Long id;

    private String name;

    private String teacherName;

    private Date startAt;

    private Date endAt;

    private String accid;

    private String token;

    private String room;

    private String teacherAccid;

    private Date timestamp = new Date();

    private List<ClassMemberDto> members = Lists.newArrayList();

}
